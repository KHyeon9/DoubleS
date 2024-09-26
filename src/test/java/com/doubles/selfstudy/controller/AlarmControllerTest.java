package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.dto.alarm.AlarmDto;
import com.doubles.selfstudy.dto.alarm.AlarmType;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.service.AlarmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AlarmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlarmService alarmService;

    @Test
    @WithMockUser
    void 알람_삭제_성공() throws Exception {
        // Given
        Long targetId = 1L;
        AlarmType alarmType = AlarmType.NEW_CHAT_MESSAGE;

        // When&Then
        mockMvc.perform(delete("/api/main/alarm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("targetId", String.valueOf(targetId))
                        .param("alarmType", String.valueOf(alarmType))
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithAnonymousUser
    void 알람_삭제시_로그인_안된_경우_에러_발생() throws Exception {
        // Given
        Long targetId = 1L;
        AlarmType alarmType = AlarmType.NEW_CHAT_MESSAGE;

        // When&Then
        mockMvc.perform(delete("/api/main/alarm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("targetId", String.valueOf(targetId))
                        .param("alarmType", String.valueOf(alarmType))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));

    }

    @Test
    @WithMockUser
    void 알람_리스트_조회_성공() throws Exception {
        // Given
        Page<AlarmDto> list = alarmService.alarmList(any(), any());

        // When
        when(list).thenReturn(Page.empty());

        // Then
        mockMvc.perform(get("/api/main/alarm")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 알람_리스트_조회시_로그인_안한_경우_에러_발생() throws Exception {
        // Given
        Page<AlarmDto> list = alarmService.alarmList(any(), any());

        // When
        when(list).thenReturn(Page.empty());

        // Then
        mockMvc.perform(get("/api/main/alarm")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }
}