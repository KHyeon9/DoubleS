package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.Chat.ChatMessageDto;
import com.doubles.selfstudy.dto.Chat.ChatRoomDto;
import com.doubles.selfstudy.entity.ChatMessage;
import com.doubles.selfstudy.entity.ChatRoom;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.ChatMessageRepository;
import com.doubles.selfstudy.repository.ChatRoomRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ServiceUtils serviceUtils;

    // 채팅룸 리스트 조회
    public List<ChatRoomDto> chatRoomList(String userId) {
        // 유저 조회
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 채팅룸 리스트 조회
        List<Object[]> results = chatRoomRepository.findAllChatRoomsByUser(userAccount, userAccount.getUserId());

        return results.stream().map(
                result -> ChatRoomDto
                        .fromEntity(
                                (ChatRoom) result[0],
                                (String) result[1],
                                (LocalDateTime) result[2]
                        )
        ).toList();
    }

    // 닉네임에 따른 채팅룸 리스트 조회
    public List<ChatRoomDto> chatRoomListByNickname(String userId, String nickname) {
        // 유저 조회
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 채팅룸 리스트를 닉네임으로 검색
        List<Object[]> results = chatRoomRepository.findAllChatRoomsByUserAndNickname(userAccount, userAccount.getUserId(), nickname);

        return results.stream().map(
                result -> ChatRoomDto
                        .fromEntity(
                                (ChatRoom) result[0],
                                (String) result[1],
                                (LocalDateTime) result[2]
                        )
        ).toList();
    }

    // 채팅룸 유저 아이디로 조회
    public ChatRoomDto getChatRoomByUserIds(String fromUserId, String toUserId) {
        return ChatRoomDto.fromEntity(
                serviceUtils.getChatRoomByUserIdsOrException(fromUserId, toUserId)
        );
    }

    // 채팅룸id로 채팅룸 조회
    public ChatRoomDto getChatRoomByChatRoomId(Long chatRoomId) {
        return ChatRoomDto.fromEntity(
                serviceUtils.getChatRoomOrException(chatRoomId)
        );
    }

    // 채팅 메세지 리스트 조회
    public List<ChatMessageDto> chatMessageListByChatRoomId(Long chatRoomId) {
        // 채팅룸 조회
        ChatRoom chatRoom = serviceUtils.getChatRoomOrException(chatRoomId);
        
        // 채팅 내용 조회
        return chatMessageRepository
                .findAllByChatRoom(chatRoom)
                .stream()
                .map(ChatMessageDto::fromEntity)
                .toList();
    }

    // 채팅룸 생성
    @Transactional
    public ChatRoomDto newChatRoom(String fromUserId, String toUserId) {
        // 채팅룸 생성하는 유저 조회
        UserAccount user1 = serviceUtils.getUserAccountOrException(fromUserId);

        // 상대 유저 조회
        UserAccount user2 = serviceUtils.getUserAccountOrException(toUserId);

        // 채팅룸 체크
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByUsers(user1, user2);

        if (chatRoom.isPresent()) {
            throw new DoubleSApplicationException(
                    ErrorCode.CHAT_ROOM_IS_EXIST,
                    "채팅방이 이미 있습니다."
            );
        }

        // 채팅룸 생성
        ChatRoom newChatRoom = ChatRoom.of(user1, user2);
        
        // 저장
        return ChatRoomDto.fromEntity(chatRoomRepository.save(newChatRoom));
    }

    // 채팅 메세지 생성
    @Transactional
    public ChatMessageDto newChatMessage(Long chatRoomId, String userId, String message) {
        // 채팅룸 조회
        ChatRoom chatRoom = serviceUtils.getChatRoomOrException(chatRoomId);

        // 유저 조회
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 메세지 생성
        ChatMessage chatMessage = ChatMessage.of(chatRoom, userAccount, message);

        return ChatMessageDto.fromEntity(chatMessageRepository.save(chatMessage));
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId, String userId) {
        // 채팅룸 찾기
        ChatRoom chatRoom = serviceUtils.getChatRoomOrException(chatRoomId);

        // 유저 찾기
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        ChatMessage message = null;
        
        if (chatRoom.getLeaveUserId() != null) {
            // 채팅방에 남은 사람이 한명이면 채팅방 삭제
            chatMessageRepository.deleteAllByChatRoom(chatRoom);
            chatRoomRepository.delete(chatRoom);
        } else if (chatRoom.getUser1() == userAccount) {
            // 유저1의 탈퇴시 상황
            chatRoom.setLeaveUserId(chatRoom.getUser1().getUserId());
            message = ChatMessage.of(chatRoom, userAccount, userAccount.getNickname() + "이 채팅방을 나갔습니다.");
        } else if (chatRoom.getUser2() == userAccount) {
            // 유저2의 탈퇴시 상황
            chatRoom.setLeaveUserId(chatRoom.getUser2().getUserId());
            message = ChatMessage.of(chatRoom, userAccount, userAccount.getNickname() + "이 채팅방을 나갔습니다.");
        }
        
        if (message != null) {
            chatMessageRepository.save(message);
        }
    }

}
