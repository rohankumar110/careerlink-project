package com.rohankumar.careerlink.userservice.services.auth;

import com.rohankumar.careerlink.userservice.dtos.auth.LoginRequestDto;
import com.rohankumar.careerlink.userservice.dtos.auth.LoginResponseDto;
import com.rohankumar.careerlink.userservice.dtos.auth.SignUpRequestDto;
import com.rohankumar.careerlink.userservice.dtos.user.UserResponseDto;

public interface AuthService {

    UserResponseDto signup(SignUpRequestDto signUpRequest);

    LoginResponseDto login(LoginRequestDto loginRequest);
}
