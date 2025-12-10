package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.SendMessageRequest;
import com.doubles.selfstudy.controller.response.ChatMessageResponse;
import com.doubles.selfstudy.dto.Chat.ChatMessageDto;
import com.doubles.selfstudy.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService chatService;
    // 해당 객체를 통해 메시지 브로커로 데이터를 전송한다
    private final SimpMessagingTemplate simpMessagingTemplate;
    final String DEFAULT_URL = "/sub/chat/room/";

    @MessageMapping("/chat/room/{chatRoomId}/entered")
    public void enteredChatRoom(
            @DestinationVariable(value = "chatRoomId") Long chatRoomId
    ) {
        log.info("# roomId : {}", chatRoomId);

        List<ChatMessageResponse> chatMessageList
                = chatService.chatMessageListByChatRoomId(chatRoomId)
                .stream()
                .map(ChatMessageResponse::fromChatMessageDto)
                .toList();

        log.info("# chatMessageList : {}", chatMessageList);

        for (ChatMessageResponse message : chatMessageList) {
            System.out.println("메시지 전송: " + message.getMessage());
            messageTemplate(chatRoomId, message);
        }
    }

    @MessageMapping("/chat/room/{chatRoomId}")
    public void sendMessage(
            @DestinationVariable(value = "chatRoomId") Long chatRoomId,
            SendMessageRequest request,
            Principal principal
    ) {
        String userId = principal.getName();
        log.info("# chatRoomId : {}", chatRoomId);
        log.info("# userId : {}", userId);
        log.info("# message : {}", request.getMessage());

        ChatMessageDto chatMessageDto =
                chatService.newChatMessage(request.getChatRoomId(), userId, request.getMessage());

        ChatMessageResponse response = ChatMessageResponse.fromChatMessageDto(chatMessageDto);

        messageTemplate(chatRoomId, response);
    }

    private void messageTemplate(Long chatRoomId, ChatMessageResponse message) {
        simpMessagingTemplate.convertAndSend(DEFAULT_URL + chatRoomId, message);
    }
}
