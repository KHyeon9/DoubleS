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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            SendMessageRequest request
    ) {
        log.info("# chatRoomId : {}", chatRoomId);
        log.info("# message : {}", request.getMessage());

        ChatMessageDto chatMessageDto =
                chatService.newChatMessage(request.getChatRoomId(), request.getSendUserId(), request.getMessage());

        ChatMessageResponse response = ChatMessageResponse.fromChatMessageDto(chatMessageDto);

        messageTemplate(chatRoomId, response);
    }


    private void messageTemplate(Long chatRoomId, ChatMessageResponse message) {
        // Map<String, Object> headerMap = messageWriterHeader(writerUserId);

        simpMessagingTemplate.convertAndSend(DEFAULT_URL + chatRoomId, message);
    }

//    private Map<String, Object> messageWriterHeader(String writerUserId) {
//        Map<String, Object> headerMap = new HashMap<>();
//        headerMap.put("userId", writerUserId);
//        return headerMap;
//    }
}
