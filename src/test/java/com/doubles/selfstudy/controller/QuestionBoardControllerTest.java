package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.QuestionBoardCreateRequest;
import com.doubles.selfstudy.controller.request.QuestionBoardModifyRequest;
import com.doubles.selfstudy.dto.post.QuestionBoardDto;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.QuestionBoardFixture;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        mockMvc.perform(post("/api/main/question_board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCreateRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 질문_게시글_작성시_로그인이_않된_경우_에러_발생() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When&Then
        mockMvc.perform(post("/api/main/question_board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardCreateRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));

    }

    @Test
    @WithMockUser
    void 포스트_수정_성공() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When
        when(questionBoardService.modifyQuestionBoard(eq(title), eq(content), any(), eq(1L)))
                .thenReturn(QuestionBoardDto.fromEntity(QuestionBoardFixture.get("userId")));

        // Then
        mockMvc.perform(put("/api/main/question_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardModifyRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithAnonymousUser
    void 포스트_수정시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When&Then
        mockMvc.perform(put("/api/main/question_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardModifyRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트_수정시_본인이_작성한_글이_아닌_경우_에러_발생() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(questionBoardService).modifyQuestionBoard(eq(title), eq(content), any(), eq(1L));

        // Then
        mockMvc.perform(put("/api/main/question_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardModifyRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트_수정시_작성한_글이_없는_경우_에러_발생() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND))
                .when(questionBoardService).modifyQuestionBoard(eq(title), eq(content), any(), eq(1L));

        // Then
        mockMvc.perform(put("/api/main/question_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new QuestionBoardModifyRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 질문_게시글_삭제_성공() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/question_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 질문_게시글_삭제시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/question_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 질문_게시글_삭제시_작성자와_삭제_요청자가_다른_경우_에러_반환() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(questionBoardService).deleteQuestionBoard(any(), any());

        // Then
        mockMvc.perform(delete("/api/main/question_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 질문_게시글_삭제시_삭제하려는_게시글이_없는_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND))
                .when(questionBoardService).deleteQuestionBoard(any(), any());

        // Then
        mockMvc.perform(delete("/api/main/question_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }
}