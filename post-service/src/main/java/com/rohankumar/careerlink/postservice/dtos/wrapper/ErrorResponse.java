package com.rohankumar.careerlink.postservice.dtos.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Boolean success;
    private Integer status;
    private String message;
    private String path;
    private String method;
    private Map<String, String> errors;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public static ErrorResponse error(int status, String message) {

        return error(status, message, null);
    }

    public static ErrorResponse error(int status, String message, Map<String, String> errors) {

        HttpServletRequest request = currentRequest();

        return ErrorResponse.builder()
                .success(false)
                .status(status)
                .message(message)
                .errors(errors)
                .path(request != null ? request.getRequestURI() : null)
                .method(request != null ? request.getMethod() : null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private static HttpServletRequest currentRequest() {

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if (attributes instanceof ServletRequestAttributes servletAttributes) {
            return servletAttributes.getRequest();
        }
        return null;
    }
}