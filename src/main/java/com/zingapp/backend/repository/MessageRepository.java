package com.zingapp.backend.repository;

import com.zingapp.backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Henter alle meldinger hvor brukeren enten er sender eller mottaker
    @Query("SELECT m FROM Message m WHERE m.sender.id = :userId OR m.receiver.id = :userId")
    List<Message> findMessagesByUser(Long userId);

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
            "(m.sender.id = :userId2 AND m.receiver.id = :userId1)")
    List<Message> findMessagesBetweenUsers(@Param("userId1") Long userId1,
                                           @Param("userId2") Long userId2);

    // Henter alle meldinger i en gitt samtale
    List<Message> findByConversationId(Long conversationId);
}