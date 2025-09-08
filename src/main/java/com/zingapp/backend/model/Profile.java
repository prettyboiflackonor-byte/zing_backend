package com.zingapp.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
public class Profile {

    @Id
    private Long id;

    private String bio;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "profile_image_id")
    private Image profileImage;

    @OneToOne
    @JoinColumn(name = "banner_image_id")
    private Image bannerImage;

}
