package com.rohankumar.careerlink.userservice.mappers.user;

import com.rohankumar.careerlink.userservice.dtos.user.UserResponseDto;
import com.rohankumar.careerlink.userservice.entities.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toResponse(User user);
}
