package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.ChatRoom;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 해당 유저가 속한 채팅방 조회
    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.user1 JOIN FETCH cr.user2 WHERE cr.user1 = :user OR cr.user2 = :user")
    List<ChatRoom> findAllChatRoomsByUser(@Param("user") UserAccount user);

    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.user1 = :user1 AND cr.user2 = :user2) OR (cr.user1 = :user2 AND cr.user2 = :user1)")
    Optional<ChatRoom> findByUsers(@Param("user1") UserAccount user1, @Param("user2") UserAccount user2);
}
