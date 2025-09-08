package com.zingapp.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private Long imageId;
    @Size(max = 300, message = "Caption must be under 300 characters.")
    private String caption;
    private LocalDateTime date;
    private Long userId;
    private Long profileId;
    private String imageUrl;

}
