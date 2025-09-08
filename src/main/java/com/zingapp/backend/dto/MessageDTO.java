package com.zingapp.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    @NotNull
    private Long conversationId;

    @NotBlank
    private String content;

    @NotNull
    private Long senderUserId;

    @NotNull
    private Long receiverUserId;

    private LocalDateTime timestamp;
    private UserDTO sender;
    private UserDTO receiver;
    private String senderProfilePicUrl;
    private String receiverProfilePicUrl;
}