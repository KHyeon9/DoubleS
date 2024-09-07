package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.ChatRoom;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.repository.ChatMessageRepository;
import com.doubles.selfstudy.repository.ChatRoomRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private ChatRoomRepository chatRoomRepository;
    @MockBean
    private ChatMessageRepository chatMessageRepository;

    @Test
    void 채팅룸_생성_성공한_경우() {
        // Given
        String userId1 = "userId1";
        String userId2 = "userId2";
        UserAccount user1 = UserAccountFixture.get(userId1, "password");
        UserAccount user2 = UserAccountFixture.get(userId2, "password");

        // When
        when(userAccountRepository.findById(userId1))
                .thenReturn(Optional.of(user1));
        when(userAccountRepository.findById(userId2))
                .thenReturn(Optional.of(user2));
        when(chatRoomRepository.findByUsers(user1, user2))
                .thenReturn(Optional.empty());

        // Then
        assertDoesNotThrow(() -> chatService.newChatRoom(userId1, userId2));
    }

    @Test
    void 채팅룸_생성시_로그인_안한_경우_에러_반환() {
        // Given
        String userId1 = "userId1";
        String userId2 = "userId2";

        // When
        when(userAccountRepository.findById(userId1))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> chatService.newChatRoom(userId1, userId2)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 채팅룸_생성시_상대_아이디가_없는_경우_에러_반환() {
        // Given
        String userId1 = "userId1";
        String userId2 = "userId2";
        UserAccount user1 = UserAccountFixture.get(userId1, "password");

        // When
        when(userAccountRepository.findById(userId1))
                .thenReturn(Optional.of(user1));
        when(userAccountRepository.findById(userId2))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> chatService.newChatRoom(userId1, userId2)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 채팅룸_생성시_채팅방이_존재하는_경우() {
        // Given
        String userId1 = "userId1";
        String userId2 = "userId2";
        UserAccount user1 = UserAccountFixture.get(userId1, "password");
        UserAccount user2 = UserAccountFixture.get(userId2, "password");

        // When
        when(userAccountRepository.findById(userId1))
                .thenReturn(Optional.of(user1));
        when(userAccountRepository.findById(userId2))
                .thenReturn(Optional.of(user2));
        when(chatRoomRepository.findByUsers(user1, user2))
                .thenReturn(Optional.of(mock(ChatRoom.class)));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> chatService.newChatRoom(userId1, userId2)
        );
        assertEquals(ErrorCode.CHAT_ROOM_IS_EXIST, e.getErrorCode());
    }

    @Test
    void 채팅_메세지_생성에_성공한_경우() {
        // Given
        Long chatRoomId = 1L;
        String userId = "userId";
        String message = "message";

        // When
        when(chatRoomRepository.findById(chatRoomId))
                .thenReturn(Optional.of(mock(ChatRoom.class)));
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));

        // Then
        assertDoesNotThrow(() -> chatService.newChatMessage(chatRoomId, userId, message));
    }

    @Test
    void 채팅_메세지_생성시_채팅룸이_없는_경우_에러_반환() {
        // Given
        Long chatRoomId = 1L;
        String userId = "userId";
        String message = "message";

        // When
        when(chatRoomRepository.findById(chatRoomId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> chatService.newChatMessage(chatRoomId, userId, message)
        );
        assertEquals(ErrorCode.CHAT_ROOM_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 채팅_메세지_생성시_로그인하지_않은_경우_에러_반환() {
        // Given
        Long chatRoomId = 1L;
        String userId = "userId";
        String message = "message";

        // When
        when(chatRoomRepository.findById(chatRoomId))
                .thenReturn(Optional.of(mock(ChatRoom.class)));
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> chatService.newChatMessage(chatRoomId, userId, message)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }
}