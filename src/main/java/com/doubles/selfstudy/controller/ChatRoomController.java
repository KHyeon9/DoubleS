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

    @PostMapping
    public Response<Void> createChatRoom(
            Authentication authentication,
            @RequestParam String toUserId
    ) {
        chatService.newChatRoom(authentication.getName(), toUserId);

        return Response.success();
    }
}
