package com.rohankumar.careerlink.postservice.services.postlikes;

public interface PostLikeService {

    void likePost(Long postId);

    void unlikePost(Long postId);
}
