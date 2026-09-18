package com.rohankumar.careerlink.postservice.dtos.post;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;

    private String content;

    private Long totalLikes;

    private Long userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
