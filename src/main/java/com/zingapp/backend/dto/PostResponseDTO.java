package com.zingapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String caption;
    private Long imageId;
    private LocalDateTime date;
    private String firstName;
    private String lastName;
    private String profilePicUrl;
    private String imageUrl;
    private Long profileId;
    private Long userId;

}
