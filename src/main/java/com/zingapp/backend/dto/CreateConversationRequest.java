package com.zingapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateConversationRequest {
    private Long userId1;
    private Long userId2;
}
