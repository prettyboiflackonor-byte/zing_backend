package com.zingapp.backend.service;

import com.zingapp.backend.dto.MessageDTO;
import com.zingapp.backend.dto.ConversationDTO;
import com.zingapp.backend.dto.UserDTO;
import com.zingapp.backend.dto.ProfileDTO;
import com.zingapp.backend.model.Message;
import com.zingapp.backend.model.User;
import com.zingapp.backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ProfileService profileService;

    public MessageService(MessageRepository messageRepository, ProfileService profileService) {
        this.messageRepository = messageRepository;
        this.profileService = profileService;
    }

    public Message createMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<MessageDTO> getMessagesByConversationId(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationId(conversationId);
        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MessageDTO convertToDTO(Message msg) {
        MessageDTO dto = new MessageDTO();
        dto.setConversationId(msg.getConversationId());
        dto.setContent(msg.getContent());
        dto.setSenderUserId(msg.getSender().getId());
        dto.setReceiverUserId(msg.getReceiver().getId());
        dto.setTimestamp(msg.getTimestamp());

        // Hent profilbilder via ProfileService
        String senderProfilePicUrl = null;
        String receiverProfilePicUrl = null;

        try {
            senderProfilePicUrl = profileService.getProfileDto(msg.getSender().getId()).getProfilePicUrl();
        } catch (Exception ignored) {}

        try {
            receiverProfilePicUrl = profileService.getProfileDto(msg.getReceiver().getId()).getProfilePicUrl();
        } catch (Exception ignored) {}

        // Lag senderDTO uten profilePicUrl
        UserDTO senderDTO = new UserDTO();
        senderDTO.setId(msg.getSender().getId());
        senderDTO.setFirstName(msg.getSender().getFirstName());
        senderDTO.setLastName(msg.getSender().getLastName());
        dto.setSender(senderDTO);

        // Lag receiverDTO uten profilePicUrl
        UserDTO receiverDTO = new UserDTO();
        receiverDTO.setId(msg.getReceiver().getId());
        receiverDTO.setFirstName(msg.getReceiver().getFirstName());
        receiverDTO.setLastName(msg.getReceiver().getLastName());
        dto.setReceiver(receiverDTO);

        // Sett profilbilder i MessageDTO
        dto.setSenderProfilePicUrl(senderProfilePicUrl);
        dto.setReceiverProfilePicUrl(receiverProfilePicUrl);

        return dto;
    }

    public List<ConversationDTO> getConversationsOverview(Long userId) {
        List<Message> allMessages = messageRepository.findMessagesByUser(userId);
        Map<Long, Message> latestMessages = new HashMap<>();

        for (Message msg : allMessages) {
            Long conversationId = msg.getConversationId();
            if (!latestMessages.containsKey(conversationId) ||
                    msg.getTimestamp().isAfter(latestMessages.get(conversationId).getTimestamp())) {
                latestMessages.put(conversationId, msg);
            }
        }

        return latestMessages.values().stream()
                .map(msg -> {
                    String otherUserName;
                    Long otherUserId;

                    if (msg.getSender().getId().equals(userId)) {
                        otherUserName = msg.getReceiver().getFirstName();
                        otherUserId = msg.getReceiver().getId();
                    } else {
                        otherUserName = msg.getSender().getFirstName();
                        otherUserId = msg.getSender().getId();
                    }

                    return new ConversationDTO(
                            msg.getConversationId(),
                            otherUserName,
                            msg.getContent(),
                            msg.getTimestamp(),
                            otherUserId
                    );
                })
                .sorted((a, b) -> b.getLatestTimestamp().compareTo(a.getLatestTimestamp()))
                .collect(Collectors.toList());
    }

    public ConversationDTO createConversation(Long userId1, Long userId2) {
        List<Message> existing = messageRepository.findMessagesBetweenUsers(userId1, userId2);
        Message latest;

        if (!existing.isEmpty()) {
            latest = existing.stream()
                    .max(Comparator.comparing(Message::getTimestamp))
                    .orElse(existing.get(0));
        } else {
            Long newConversationId = System.currentTimeMillis();
            Message newMessage = new Message();
            newMessage.setConversationId(newConversationId);
            newMessage.setContent("Zing!");
            newMessage.setTimestamp(LocalDateTime.now());

            User sender = new User();
            sender.setId(userId1);
            newMessage.setSender(sender);

            User receiver = new User();
            receiver.setId(userId2);
            newMessage.setReceiver(receiver);

            latest = messageRepository.save(newMessage);
        }

        Long otherUserId = latest.getSender().getId().equals(userId1)
                ? latest.getReceiver().getId()
                : latest.getSender().getId();

        String otherUserName = latest.getSender().getId().equals(userId1)
                ? latest.getReceiver().getFirstName()
                : latest.getSender().getFirstName();

        return new ConversationDTO(
                latest.getConversationId(),
                otherUserName,
                latest.getContent(),
                latest.getTimestamp(),
                otherUserId
        );
    }
}