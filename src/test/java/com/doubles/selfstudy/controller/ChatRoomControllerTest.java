package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.dto.Chat.ChatRoomDto;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ChatRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;

    @Test
    @WithMockUser
    void 채팅룸_생성_성공() throws Exception {
        // Given
        String toUserId = "toUserId";

        // When & Then
        mockMvc.perform(post("/api/main/chat/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("toUserId", toUserId)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 채팅룸_생성시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given
        String toUserId = "toUserId";

        // When & Then
        mockMvc.perform(post("/api/main/chat/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("toUserId", toUserId)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 채팅룸_삭제_성공() throws Exception {
        // Given
        Long chatRoomId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/main/chat/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("chatRoomId", String.valueOf(chatRoomId))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 채팅룸_삭제시_로그인_않한_경우_에러_발생() throws Exception {
        // Given
        Long chatRoomId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/main/chat/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("chatRoomId", String.valueOf(chatRoomId))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 채팅룸_조회_성공() throws Exception {
        // Given
        List<ChatRoomDto> chatRoomList = chatService.chatRoomList(any());

        // When
        when(chatRoomList).thenReturn(List.of());

        // Then
        mockMvc.perform(get("/api/main/chat/room")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 채팅룸시_로그인_안한_경우_에러_발생() throws Exception {
        // Given
        List<ChatRoomDto> chatRoomList = chatService.chatRoomList(any());

        // When
        when(chatRoomList).thenReturn(List.of());

        // Then
        mockMvc.perform(get("/api/main/chat/room")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 채팅룸을_닉네임으로_조회_성공() throws Exception {
        // Given
        List<ChatRoomDto> chatRoomList = chatService.chatRoomListByNickname(any(), any());

        // When
        when(chatRoomList).thenReturn(List.of());

        // Then
        mockMvc.perform(get("/api/main/chat/room/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 채팅룸을_닉네임으로_조회시_로그인_안할시_에러_발생() throws Exception {
        // Given
        List<ChatRoomDto> chatRoomList = chatService.chatRoomListByNickname(any(), any());

        // When
        when(chatRoomList).thenReturn(List.of());

        // Then
        mockMvc.perform(get("/api/main/chat/room/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }
}