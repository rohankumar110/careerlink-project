package com.rohankumar.careerlink.postservice.dtos.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequestDto {

    @NotBlank(message = "Post content cannot be empty")
    @Size(max = 3000, message = "Post content must not exceed 3000 characters")
    private String content;
}
