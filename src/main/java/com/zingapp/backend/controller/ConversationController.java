package com.zingapp.backend.controller;

import com.zingapp.backend.dto.ConversationDTO;
import com.zingapp.backend.dto.CreateConversationRequest;
import com.zingapp.backend.dto.MessageDTO;
import com.zingapp.backend.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final MessageService messageService;

    public ConversationController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Endepunkt for å hente samtaleoversikt for en gitt bruker
    // Forventet kall fra frontend: GET /api/conversations?userId=1
    @GetMapping
    public List<ConversationDTO> getConversationsOverview(@RequestParam Long userId) {
        return messageService.getConversationsOverview(userId);
    }


    @PostMapping
    public ConversationDTO createConversation(@RequestBody CreateConversationRequest request) {
        return messageService.createConversation(request.getUserId1(), request.getUserId2());
    }

    @GetMapping("/conversation/{conversationId}")
    public List<MessageDTO> getMessagesByConversationId(@PathVariable Long conversationId) {
        return messageService.getMessagesByConversationId(conversationId);
    }

    @GetMapping("/{conversationId}")
    public List<MessageDTO> getMessages(@PathVariable Long conversationId) {
        return messageService.getMessagesByConversationId(conversationId);
    }


}