package com.rohankumar.careerlink.postservice.controllers.postlikes;

import com.rohankumar.careerlink.postservice.services.postlikes.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {

        postLikeService.likePost(postId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> unLikePost(@PathVariable Long postId) {

        postLikeService.unlikePost(postId);
        return ResponseEntity.noContent().build();
    }
}
