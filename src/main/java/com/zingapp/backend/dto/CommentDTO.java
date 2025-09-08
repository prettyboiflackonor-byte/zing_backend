package com.zingapp.backend.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private String comment;
    private String firstName;
    private String lastName;
    private Long profileId;
    private String profilePicUrl;
}
