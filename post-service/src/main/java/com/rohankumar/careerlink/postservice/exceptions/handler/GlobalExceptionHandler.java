package com.rohankumar.careerlink.postservice.exceptions.handler;

import com.rohankumar.careerlink.postservice.dtos.wrapper.ErrorResponse;
import com.rohankumar.careerlink.postservice.exceptions.BadRequestException;
import com.rohankumar.careerlink.postservice.exceptions.ForbiddenException;
import com.rohankumar.careerlink.postservice.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {

        log.warn("Resource Not Found Error: {}", ex.getMessage());

        return ResponseEntity.status(NOT_FOUND).body(
                ErrorResponse.error(NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {

        log.warn("Bad Request Error: {}", ex.getMessage());

        return ResponseEntity.status(BAD_REQUEST).body(
                ErrorResponse.error(BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {

        log.warn("Forbidden Error: {}", ex.getMessage());

        return ResponseEntity.status(FORBIDDEN).body(
                ErrorResponse.error(FORBIDDEN.value(), ex.getMessage()));
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {

        return validationErrorResponse(ex.getBindingResult(), headers);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex) {

        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing
                ));

        log.warn("Constraint Violation Error: {}", errors);

        return ResponseEntity.status(BAD_REQUEST).body(
                ErrorResponse.error(BAD_REQUEST.value(), "Validation Error", errors));
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {

        log.warn("Unreadable Request Body Error: {}", ex.getMessage());

        return new ResponseEntity<>(
                ErrorResponse.error(BAD_REQUEST.value(), "Malformed or Missing Request Body"),
                headers, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {

        log.warn("Method Not Supported Error: {}", ex.getMessage());

        String message = "Request method '" + ex.getMethod() + "' is not supported for this Endpoint";
        return new ResponseEntity<>(
                ErrorResponse.error(METHOD_NOT_ALLOWED.value(), message),
                headers, METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {

        log.warn("Missing Request Parameter Error: {}", ex.getMessage());

        String message = "Required Parameter '" + ex.getParameterName() + "' is Missing";
        return new ResponseEntity<>(
                ErrorResponse.error(BAD_REQUEST.value(), message), headers, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {

        log.warn("No Resource Found Error: {}", ex.getMessage());

        return new ResponseEntity<>(
                ErrorResponse.error(NOT_FOUND.value(), "The Requested Resource was not Found"),
                headers, NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex, @Nullable Object body, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {

        if (statusCode.is5xxServerError()) {
            log.error("Server Error [{}]", statusCode.value(), ex);
        } else {
            log.warn("Request Error [{}]: {}", statusCode.value(), ex.getMessage());
        }

        String message = statusCode.is5xxServerError()
                ? "Server Error. Please try again"
                : ex.getMessage();

        return new ResponseEntity<>(
                ErrorResponse.error(statusCode.value(), message), headers, statusCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex) {

        log.error("Internal Server Error", ex);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ErrorResponse.error(INTERNAL_SERVER_ERROR.value(), "Server Error. Please try again"));
    }

    private ResponseEntity<Object> validationErrorResponse(BindingResult bindingResult,
                                                           HttpHeaders headers) {

        Map<String, String> errors = bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        err -> Objects.requireNonNullElse(err.getDefaultMessage(), "Invalid value"),
                        (existing, replacement) -> existing
                ));

        log.warn("Validation Error: {}", errors);

        return new ResponseEntity<>(
                ErrorResponse.error(BAD_REQUEST.value(), "Validation Error", errors),
                headers, BAD_REQUEST);
    }
}