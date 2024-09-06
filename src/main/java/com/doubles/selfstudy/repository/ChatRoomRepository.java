package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.ChatRoom;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 해당 유저가 속한 채팅방 조회
    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.user1 JOIN FETCH cr.user2 WHERE cr.user1 = :user OR cr.user2 = :user")
    List<ChatRoom> findChatRoomsByUser(@Param("user") UserAccount user);
}
