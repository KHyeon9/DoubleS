package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.ChatMessage;
import com.doubles.selfstudy.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 채팅룸의 메세지 조회
    List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);

    // 가장 마지막으로 온 메세지 1개 조회, jpql은 limit를 사용 못하기 때문에 nativeQuery 사용
    @Query(value = "SELECT * FROM chat_message WHERE room_id = :chatRoomId ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    Optional<ChatMessage> findLastMessageByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    // 같은 채팅룸의 메세지 모두 삭제
    void deleteAllByChatRoom(ChatRoom chatRoom);
}
