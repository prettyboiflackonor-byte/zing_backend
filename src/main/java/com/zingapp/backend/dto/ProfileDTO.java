package com.zingapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    private Long id;
    private String bio;
    private String profilePicUrl;
    private String bannerUrl;
    private String firstName;
    private String lastName;
    private String email;
}
