package com.rohankumar.careerlink.postservice.services.post.impl;

import com.rohankumar.careerlink.postservice.dtos.wrapper.PaginationResponse;
import com.rohankumar.careerlink.postservice.exceptions.ResourceNotFoundException;
import com.rohankumar.careerlink.postservice.dtos.post.PostCreateRequestDto;
import com.rohankumar.careerlink.postservice.dtos.post.PostResponseDto;
import com.rohankumar.careerlink.postservice.entities.post.Post;
import com.rohankumar.careerlink.postservice.mappers.post.PostMapper;
import com.rohankumar.careerlink.postservice.repositories.post.PostRepository;
import com.rohankumar.careerlink.postservice.services.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper  postMapper;
    private final PostRepository postRepository;

    @Override
    public PostResponseDto createPost(PostCreateRequestDto postCreateRequest) {

        Long userId = 1L;
        log.info("Creating new Post for userId: {}", userId);

        Post newPost = postMapper.toEntity(postCreateRequest);
        newPost.setUserId(userId);

        log.info("Saving Post");
        Post savedPost = postRepository.save(newPost);
        log.info("Post saved successfully with id: {} for userId: {}", savedPost.getId(), userId);

        return postMapper.toResponse(savedPost);
    }

    @Override
    public PostResponseDto getPostById(Long id) {

        log.info("Getting Post by id: {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        log.info("Post found successfully with id: {} for userId: {}", post.getId(), post.getUserId());
        return postMapper.toResponse(post);
    }

    @Override
    public PaginationResponse<PostResponseDto> getAllPostsByUser(Long userId, Pageable pageable) {

        log.info("Getting Posts by userId: {}", userId);

        Page<Post> posts = postRepository.findAllByUserId(userId, pageable);

        log.info("Posts found successfully for userId: {}", userId);

        log.info("Found {} posts for userId: {} (page {} of {})",
                posts.getNumberOfElements(), userId, posts.getNumber(), posts.getTotalPages());

        return PaginationResponse.makeResponse(posts, postMapper::toResponse);
    }
}
