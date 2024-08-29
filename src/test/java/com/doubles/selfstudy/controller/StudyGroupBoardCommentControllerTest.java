package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.StudyGroupBoardCommentRequest;
import com.doubles.selfstudy.dto.studygroup.StudyGroupBoardCommentDto;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.StudyGroupBoardCommentFixture;
import com.doubles.selfstudy.service.StudyGroupBoardCommentService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class StudyGroupBoardCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudyGroupBoardCommentService studyGroupBoardCommentService;

    @Test
    @WithMockUser
    void 스터디_그룹_게시글_댓글_작성이_성공한_경우() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(post("/api/main/study_group/board/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_게시글_댓글_작성시_로그인을_안한_경우_에러_발생() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(post("/api/main/study_group/board/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글_댓글_작성시_게시글이_없는_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND))
                .when(studyGroupBoardCommentService).createStudyGroupBoardCommnet(any(), any(), any());

        // Then
        mockMvc.perform(post("/api/main/study_group/board/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글_댓글_수정이_성공한_경우() throws Exception {
        // Given

        // When
        when(studyGroupBoardCommentService.modifyStudyGroupBoardComment(any(), eq(1L), any()))
                .thenReturn(
                        StudyGroupBoardCommentDto.fromEntity(
                            StudyGroupBoardCommentFixture.get("userId", "comment")
                        )
                );

        // Then
        mockMvc.perform(put("/api/main/study_group/board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_게시글_댓글_수정시_로그인이_안된_경우_에러_발생() throws Exception {
        // Given

        // When
        when(studyGroupBoardCommentService.modifyStudyGroupBoardComment(any(), eq(1L), any()))
                .thenReturn(
                        StudyGroupBoardCommentDto.fromEntity(
                                StudyGroupBoardCommentFixture.get("userId", "comment")
                        )
                );

        // Then
        mockMvc.perform(put("/api/main/study_group/board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글_댓글_수정시_댓글이_없는_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.COMMENT_NOT_FOUND))
                .when(studyGroupBoardCommentService).modifyStudyGroupBoardComment(any(), any(), any());

        // Then
        mockMvc.perform(put("/api/main/study_group/board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.COMMENT_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글_댓글_수정시_작성자와_다른_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(studyGroupBoardCommentService).modifyStudyGroupBoardComment(any(), any(), any());

        // Then
        mockMvc.perform(put("/api/main/study_group/board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글_댓글_삭제가_성공한_경우() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/study_group/board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_게시글_댓글_삭제시_로그인_하지_않은_경우_에러_발생() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/study_group/board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글_댓글_삭제시_댓글이_없는_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.COMMENT_NOT_FOUND))
                .when(studyGroupBoardCommentService).deleteStudyGroupBoardComment(any(), any());

        // Then
        mockMvc.perform(delete("/api/main/study_group/board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.COMMENT_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글_댓글_삭제시_작성자와_다른_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(studyGroupBoardCommentService).deleteStudyGroupBoardComment(any(), any());

        // Then
        mockMvc.perform(delete("/api/main/study_group/board/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글_댓글_리스드_조회_성공한_경우() throws Exception {
        // Given
        Page<StudyGroupBoardCommentDto> list = studyGroupBoardCommentService.studyGroupBoardCommentList(any(), any());

        // When
        when(list).thenReturn(Page.empty());

        // Then
        mockMvc.perform(get("/api/main/study_group/board/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_게시글_댓글_리스드_조회시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given
        Page<StudyGroupBoardCommentDto> list = studyGroupBoardCommentService.studyGroupBoardCommentList(any(), any());

        // When
        when(list).thenReturn(Page.empty());

        // Then
        mockMvc.perform(get("/api/main/study_group/board/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }
}