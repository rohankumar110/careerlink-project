package com.rohankumar.careerlink.userservice.services.auth.impl;

import com.rohankumar.careerlink.userservice.dtos.auth.LoginRequestDto;
import com.rohankumar.careerlink.userservice.dtos.auth.LoginResponseDto;
import com.rohankumar.careerlink.userservice.dtos.auth.SignUpRequestDto;
import com.rohankumar.careerlink.userservice.dtos.user.UserResponseDto;
import com.rohankumar.careerlink.userservice.entities.user.User;
import com.rohankumar.careerlink.userservice.exceptions.BadRequestException;
import com.rohankumar.careerlink.userservice.mappers.user.UserMapper;
import com.rohankumar.careerlink.userservice.repositories.user.UserRepository;
import com.rohankumar.careerlink.userservice.security.JWTService;
import com.rohankumar.careerlink.userservice.services.auth.AuthService;
import com.rohankumar.careerlink.userservice.utils.PasswordHasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JWTService jwtService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponseDto signup(SignUpRequestDto signUpRequest) {

        log.info("Signing up user with email: {}", signUpRequest.getEmail());

        Optional<User> optionalUserByEmail = userRepository.findByEmail(signUpRequest.getEmail());
        if(optionalUserByEmail.isPresent()) {
            log.info("User with email: {} already exists", signUpRequest.getEmail());
            throw new BadRequestException("Email already exists");
        }

        User newUser = new User();
        newUser.setName(signUpRequest.getName());
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setPassword(PasswordHasher.hash(signUpRequest.getPassword()));

        log.info("Saving user");
        User savedUser = userRepository.save(newUser);
        log.info("User signed up successfully with email: {}", signUpRequest.getEmail());

        return userMapper.toResponse(savedUser);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequest) {

        log.info("Authenticating user with email: {}", loginRequest.getEmail());

        Optional<User> optionalUserByEmail = userRepository.findByEmail(loginRequest.getEmail());
        if(optionalUserByEmail.isEmpty()) {
            log.info("No User found with Email: {}", loginRequest.getEmail());
            throw new BadRequestException("Invalid Email or Password");
        }

        log.info("Verifying User Credentials");
        boolean isPasswordMatched = PasswordHasher.matches(loginRequest.getPassword(), optionalUserByEmail.get().getPassword());
        if(!isPasswordMatched) {
            log.info("Password does not match");
            throw new BadRequestException("Invalid Email or Password");
        }

        log.info("User Credentials verified successfully");

        log.info("Generating Access Token");
        String accessToken = jwtService.generateAccessToken(optionalUserByEmail.get());
        log.info("Access Token generated successfully");

        return new LoginResponseDto(accessToken);
    }
}
