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
    @Query("SELECT DISTINCT cr, cm.message, cm.createdAt " +
            "FROM ChatRoom cr " +
            "LEFT JOIN FETCH cr.user1 u1 " +
            "LEFT JOIN FETCH cr.user2 u2 " +
            "LEFT JOIN ChatMessage cm ON cm.chatRoom = cr AND cm.createdAt = " +
            "       (SELECT MAX(cm2.createdAt) FROM ChatMessage cm2 WHERE cm2.chatRoom = cr) " +
            "WHERE (u1 = :user OR u2 = :user) " +
            "AND (cr.leaveUserId IS NULL OR cr.leaveUserId != :userId)")
    List<Object[]> findAllChatRoomsByUser(@Param("user") UserAccount user, @Param("userId") String userId);

    // 유저 닉네임으로 검색
    @Query("SELECT DISTINCT cr, cm.message, cm.createdAt " +
            "FROM ChatRoom cr " +
            "LEFT JOIN FETCH cr.user1 u1 " +
            "LEFT JOIN FETCH cr.user2 u2 " +
            "LEFT JOIN ChatMessage cm ON cm.chatRoom = cr AND cm.createdAt = " +
            "       (SELECT MAX(cm2.createdAt) FROM ChatMessage cm2 WHERE cm2.chatRoom = cr) " +
            "WHERE (u1 = :user OR u2 = :user) " +
            "AND (CASE WHEN u1 = :user THEN u2.nickname ELSE u1.nickname END) LIKE %:nickname% " +
            "AND (cr.leaveUserId IS NULL OR cr.leaveUserId != :userId)")
    List<Object[]> findAllChatRoomsByUserAndNickname(@Param("user") UserAccount user, @Param("userId") String userId, @Param("nickname") String nickname);
    
    // 유저 엔티티로 채팅룸 검색
    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.user1 = :user1 AND cr.user2 = :user2) OR (cr.user1 = :user2 AND cr.user2 = :user1)")
    Optional<ChatRoom> findByUsers(@Param("user1") UserAccount user1, @Param("user2") UserAccount user2);

    // 유저id로 채팅룸 검색
    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.user1.userId = :userId1 AND cr.user2.userId = :userId2) OR (cr.user1.userId = :userId2 AND cr.user2.userId = :userId1)")
    Optional<ChatRoom> findByUserIds(@Param("userId1") String userId1, @Param("userId2") String userId2);

    // 유저 엔티티로 관련된 모든 채팅룸 검색
    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.user1 = :user) OR (cr.user2 = :user)")
    List<ChatRoom> findAllByUser(@Param("user") UserAccount user);
}
