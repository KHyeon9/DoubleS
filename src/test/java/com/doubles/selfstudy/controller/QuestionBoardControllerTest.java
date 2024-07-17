package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.QuestionBoardCreaeteRequest;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.service.QuestionBoardService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class QuestionBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuestionBoardService questionBoardService;

    @Test
    @WithMockUser
    void 질문_게시글_작성_성공() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When&Then
        mockMvc.perform(post("/api/main/post/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCreaeteRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 질문_게시글_작성시_로그인이_않된_경우() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When&Then
        mockMvc.perform(post("/api/main/post/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCreaeteRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }
}