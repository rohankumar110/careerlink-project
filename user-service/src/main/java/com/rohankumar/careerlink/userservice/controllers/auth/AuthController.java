package com.rohankumar.careerlink.userservice.controllers.auth;

import com.rohankumar.careerlink.userservice.dtos.auth.LoginRequestDto;
import com.rohankumar.careerlink.userservice.dtos.auth.LoginResponseDto;
import com.rohankumar.careerlink.userservice.dtos.auth.SignUpRequestDto;
import com.rohankumar.careerlink.userservice.dtos.user.UserResponseDto;
import com.rohankumar.careerlink.userservice.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody SignUpRequestDto signUpRequest) {

        UserResponseDto userResponse = authService.signup(signUpRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {

        LoginResponseDto loginResponse = authService.login(loginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);

    }
}
