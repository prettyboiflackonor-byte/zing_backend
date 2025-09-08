package com.zingapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ConversationDTO {

    private Long conversationId;

    private String otherUserName;

    private String latestMessage;

    private LocalDateTime latestTimestamp;

    private Long otherUserId;

    public ConversationDTO(Long conversationId, String otherUserName,
                           String latestMessage, LocalDateTime latestTimestamp,  Long otherUserId) {
        this.conversationId = conversationId;
        this.otherUserName = otherUserName;
        this.latestMessage = latestMessage;
        this.latestTimestamp = latestTimestamp;
        this.otherUserId = otherUserId;
    }
}