package com.rohankumar.careerlink.postservice.services.postlikes.impl;

import com.rohankumar.careerlink.postservice.entities.postlikes.PostLike;
import com.rohankumar.careerlink.postservice.entities.post.Post;
import com.rohankumar.careerlink.postservice.exceptions.BadRequestException;
import com.rohankumar.careerlink.postservice.exceptions.ResourceNotFoundException;
import com.rohankumar.careerlink.postservice.repositories.postlikes.PostLikeRepository;
import com.rohankumar.careerlink.postservice.repositories.post.PostRepository;
import com.rohankumar.careerlink.postservice.services.postlikes.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    @Transactional
    public void likePost(Long postId) {

        Long userId = 1L;
        log.info("Liking post with id {} for userId: {}", postId, userId);

        log.info("Checking if exists by id: {}", postId);

        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        log.info("Checking if post is already liked by userId: {}", userId);
        boolean hasAlreadyLiked = postLikeRepository.existsByPostIdAndUserId(postId, userId);

        if(hasAlreadyLiked) {
            log.warn("Post has already liked by userId: {}", userId);
            throw new BadRequestException("Post has already been liked");
        }

        PostLike postLike = new PostLike();
        postLike.setUserId(userId);
        postLike.setPostId(postId);

        log.info("Saving the post like");

        postLikeRepository.save(postLike);

        foundPost.setTotalLikes(foundPost.getTotalLikes() + 1);
        log.info("Post has been liked successfully");
    }

    @Override
    @Transactional
    public void unlikePost(Long postId) {

        Long userId = 1L;
        log.info("Unliking post with id {} for userId: {}", postId, userId);

        log.info("Checking if exists Post by id: {}", postId);

        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        log.info("Checking if post is already unliked by userId: {}", userId);
        boolean alreadyLiked = postLikeRepository.existsByPostIdAndUserId(postId, userId);

        if(!alreadyLiked) {
            log.warn("Post {} is not liked by userId: {}", postId, userId);
            throw new BadRequestException("You have not liked this post");
        }

        log.info("Deleting the post like");

        postLikeRepository.deleteByPostIdAndUserId(postId, userId);

        foundPost.setTotalLikes(foundPost.getTotalLikes() - 1);

        log.info("Post has been unliked successfully");
    }
}
