package com.rohankumar.careerlink.postservice.controllers.post;

import com.rohankumar.careerlink.postservice.dtos.post.PostCreateRequestDto;
import com.rohankumar.careerlink.postservice.dtos.post.PostResponseDto;
import com.rohankumar.careerlink.postservice.dtos.wrapper.PaginationResponse;
import com.rohankumar.careerlink.postservice.services.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostCreateRequestDto postCreateRequest) {

        PostResponseDto postResponse = postService.createPost(postCreateRequest);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {

        PostResponseDto postResponse = postService.getPostById(id);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<PaginationResponse<PostResponseDto>> getAllPostsByUser(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        PaginationResponse<PostResponseDto> userPostsPage = postService.getAllPostsByUser(userId, pageable);
        return new ResponseEntity<>(userPostsPage, HttpStatus.OK);
    }
}
