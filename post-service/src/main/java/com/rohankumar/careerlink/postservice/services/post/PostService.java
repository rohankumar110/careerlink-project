package com.rohankumar.careerlink.postservice.services.post;

import com.rohankumar.careerlink.postservice.dtos.post.PostCreateRequestDto;
import com.rohankumar.careerlink.postservice.dtos.post.PostResponseDto;
import com.rohankumar.careerlink.postservice.dtos.wrapper.PaginationResponse;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostResponseDto createPost(PostCreateRequestDto postCreateRequest);

    PostResponseDto getPostById(Long id);

    PaginationResponse<PostResponseDto> getAllPostsByUser(Long userId, Pageable pageable);
}
