package com.rohankumar.careerlink.postservice.mappers.post;

import com.rohankumar.careerlink.postservice.dtos.post.PostCreateRequestDto;
import com.rohankumar.careerlink.postservice.dtos.post.PostResponseDto;
import com.rohankumar.careerlink.postservice.entities.post.Post;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostResponseDto toResponse(Post post);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "content", source = "content")
    Post toEntity(PostCreateRequestDto postCreateRequest);
}

