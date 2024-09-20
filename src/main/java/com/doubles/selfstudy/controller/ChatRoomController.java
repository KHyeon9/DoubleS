package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.response.ChatRoomResponse;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.dto.Chat.ChatRoomDto;
import com.doubles.selfstudy.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/main/chat/room")
@RestController
public class ChatRoomController {

    private final ChatService chatService;

    // 채팅룸 리스트 반환
    @GetMapping
    public Response<List<ChatRoomResponse>> chatRoomList(
            Authentication authentication
    ) {
        List<ChatRoomDto> chatRoomList = chatService.chatRoomList(authentication.getName());

        return Response.success(
                chatRoomList
                        .stream()
                        .map(ChatRoomResponse::fromChatRoomDto)
                        .toList()
        );
    }

    // 채팅룸 리스트 닉네임으로 검색
    @GetMapping("/{nickname}")
    public Response<List<ChatRoomResponse>> chatRoomList(
            Authentication authentication,
            @PathVariable String nickname
    ) {
        List<ChatRoomDto> chatRoomList = chatService.chatRoomListByNickname(authentication.getName(), nickname);

        return Response.success(
                chatRoomList
                        .stream()
                        .map(ChatRoomResponse::fromChatRoomDto)
                        .toList()
        );
    }

    // 채팅룸이 존재하면 조회
    @GetMapping("/user/{toUserId}")
    public Response<ChatRoomResponse> getChatRoomByToUserId(
            Authentication authentication,
            @PathVariable String toUserId
    ) {
        return Response.success(
                ChatRoomResponse.fromChatRoomDto(
                    chatService.getChatRoomByUserIds(authentication.getName(), toUserId)
                )
        );
    }

    // 채팅룸 id로 채팅룸 데이터 조회
    @GetMapping("/id/{chatRoomId}")
    public Response<ChatRoomResponse> getChatRoomByChatRoomId(
            @PathVariable Long chatRoomId
    ) {
        return Response.success(
                ChatRoomResponse.fromChatRoomDto(
                        chatService.getChatRoomByChatRoomId(chatRoomId)
                )
        );
    }

    // 채팅룸 생성
    @PostMapping
    public Response<ChatRoomResponse> createChatRoom(
            Authentication authentication,
            @RequestParam String toUserId
    ) {
        return Response.success(
                ChatRoomResponse.fromChatRoomDto(
                    chatService.newChatRoom(authentication.getName(), toUserId)
                )
        );
    }

    // 채팅룸 삭제
    @DeleteMapping
    public Response<Void> deleteChatRoom(
            Authentication authentication,
            @RequestParam Long chatRoomId
    ) {
        chatService.deleteChatRoom(chatRoomId, authentication.getName());

        return Response.success();
    }
}
