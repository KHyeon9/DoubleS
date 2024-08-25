package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.QuestionBoardCommentRequest;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.service.QuestionBoardCommentService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class QuestionBoardCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuestionBoardCommentService questionBoardCommentService;

    @Test
    @WithMockUser
    void 질문_게시글_댓글_작성_성공() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(post("/api/main/question_board/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 질문_게시글_댓글_작성시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(post("/api/main/question_board/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 질문_게시글_댓글_작성시_게시물이_없는_경우_에러_반환() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND))
                .when(questionBoardCommentService).createQuestionBoardComment(any(), any(), any());

        // Then
        mockMvc.perform(post("/api/main/question_board/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 질문_게시글_댓글_수정_성공() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(put("/api/main/question_board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 질문_게시글_댓글_수정시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(put("/api/main/question_board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 질문_게시글_댓글_수정시_게시글이_없는_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND))
                .when(questionBoardCommentService).modifyQuestionBoardComment(any(), any(), any());

        // Then
        mockMvc.perform(put("/api/main/question_board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 질문_게시글_댓글_삭제_성공() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/question_board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 질문_게시글_댓글_삭제시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/question_board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 질문_게시글_댓글_삭제시_게시글이_없는_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND))
                .when(questionBoardCommentService).deleteQuestionBoardComment(any(), any());

        // Then
        mockMvc.perform(delete("/api/main/question_board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }
}