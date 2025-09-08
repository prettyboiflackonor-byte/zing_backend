package com.zingapp.backend.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    private Long id;
    private Long postId;
    private Long userId;
}
