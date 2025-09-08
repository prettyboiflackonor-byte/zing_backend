package com.zingapp.backend.controller;

import com.zingapp.backend.dto.ConversationDTO;
import com.zingapp.backend.dto.MessageDTO;
import com.zingapp.backend.model.Message;
import com.zingapp.backend.model.User;
import com.zingapp.backend.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // POST: Opprett en ny melding
    @PostMapping
    public ResponseEntity<?> createMessage(@RequestBody MessageDTO dto) {
        // Enkel manuell validering av lengde
        if (dto.getContent() != null && dto.getContent().length() > 255) {
            return ResponseEntity
                    .badRequest()
                    .body("Max 255 characters!");
        }

        User sender = new User();
        sender.setId(dto.getSenderUserId());

        User receiver = new User();
        receiver.setId(dto.getReceiverUserId());

        Message message = new Message(
                dto.getConversationId(),
                dto.getContent(),
                LocalDateTime.now(),
                sender,
                receiver
        );

        return ResponseEntity.ok(messageService.createMessage(message));
    }

    // GET: Hent alle meldinger i en spesifikk samtale
    @GetMapping("/conversation/{id}")
    public List<MessageDTO> getMessagesByConversation(@PathVariable Long id) {
        return messageService.getMessagesByConversationId(id);
    }

    // GET: Hent samtaleoversikt (nyeste melding per samtale) for en bruker
    @GetMapping("/conversations")
    public List<ConversationDTO> getConversationsOverview(@RequestParam Long userId) {
        return messageService.getConversationsOverview(userId);
    }
}