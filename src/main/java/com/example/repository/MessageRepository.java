package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    Integer deleteByMessageId(int messageId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Message m SET m.messageText = :messageText WHERE m.messageId = :messageId")
    Integer updateByMessageIdAndMessageText(int messageId, String messageText);
}
