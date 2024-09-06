package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.ChatMessage;
import com.doubles.selfstudy.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 채팅룸의 메세지 조회
    List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);
}
