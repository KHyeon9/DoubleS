package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.Chat.ChatMessageDto;
import com.doubles.selfstudy.dto.Chat.ChatRoomDto;
import com.doubles.selfstudy.repository.ChatRoomRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ObjectMapper objectMapper;
    private final ServiceUtils serviceUtils;

    // 채팅룸 리스트 조회
    public List<ChatRoomDto> chatRoomList(String userId) {
        return List.of();
    }

    // 채팅 메세지 리스트 조회
    public List<ChatMessageDto> chatMessageListByChatRoomId(Long chatRoomId) {
        return List.of();
    }

    // 채팅룸 생성
    @Transactional
    public void newChatRoom(String fromUserId, String toUserId) {

    }

    // 채팅 메세지 생성
    @Transactional
    public void insertChatMessage(Long chatRoomId, String userId, String message) {
        
    }

}
