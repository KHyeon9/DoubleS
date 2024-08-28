package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.StudyGroupBoardRequest;
import com.doubles.selfstudy.dto.studygroup.StudyGroupBoardDto;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.StudyGroupBoardFixture;
import com.doubles.selfstudy.service.StudyGroupBoardService;
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
class StudyGroupBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private StudyGroupBoardService studyGroupBoardService;
    
    @Test
    @WithMockUser
    void 스터디_그룹_게시글의_작성이_성공한_경우() throws Exception {
        // Given
        String title = "title";
        String content = "content";
        
        // When & Then
        mockMvc.perform(post("/api/main/study_group/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_게시글의_작성시_로그인이_안된_경우_에러_발생() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When & Then
        mockMvc.perform(post("/api/main/study_group/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글의_작성시_스터디_그룹이_없는_경우_에러_발생() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.USER_STUDY_GROUP_NOT_FOUND))
                .when(studyGroupBoardService).createStudyGroupBoard(any(), eq(title), eq(content));


        // Then
        mockMvc.perform(post("/api/main/study_group/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.USER_STUDY_GROUP_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글의_수정이_성공한_경우() throws Exception {
        // Given
        String title = "modify_title";
        String content = "modify_content";

        // When
        when(studyGroupBoardService.modifyStudyGroupBoard(any(), eq(1L), eq(title), eq(content)))
                .thenReturn(StudyGroupBoardDto.fromEntity(StudyGroupBoardFixture.get("userId", title, content)));

        // Then
        mockMvc.perform(put("/api/main/study_group/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_게시글의_수정시_로그인이_안된_경우_에러_반환() throws Exception {
        // Given
        String title = "modify_title";
        String content = "modify_content";

        // When
        when(studyGroupBoardService.modifyStudyGroupBoard(any(), eq(1L), eq(title), eq(content)))
                .thenReturn(StudyGroupBoardDto.fromEntity(StudyGroupBoardFixture.get("userId", title, content)));

        // Then
        mockMvc.perform(put("/api/main/study_group/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글의_수정시_게시글이_없는_경우_에러_반환() throws Exception {
        // Given
        String title = "modify_title";
        String content = "modify_content";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND))
                .when(studyGroupBoardService).modifyStudyGroupBoard(any(), eq(1L), eq(title), eq(content));

        // Then
        mockMvc.perform(put("/api/main/study_group/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글의_수정시_작성자가_다른_경우_에러_반환() throws Exception {
        // Given
        String title = "modify_title";
        String content = "modify_content";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(studyGroupBoardService).modifyStudyGroupBoard(any(), eq(1L), eq(title), eq(content));

        // Then
        mockMvc.perform(put("/api/main/study_group/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new StudyGroupBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글의_삭제가_성공한_경우() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/study_group/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_게시글의_삭제시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/study_group/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글의_삭제시_게시글이_없는_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND))
                .when(studyGroupBoardService).deleteStudyGroupBoard(any(), eq(1L));

        // Then
        mockMvc.perform(delete("/api/main/study_group/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글의_삭제시_작성자가_다른_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(studyGroupBoardService).deleteStudyGroupBoard(any(), eq(1L));

        // Then
        mockMvc.perform(delete("/api/main/study_group/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글_리스트_조회_성공한_경우() throws Exception {
        // Given
        Page<StudyGroupBoardDto> list = studyGroupBoardService.studyGroupBoardList(any(), any());

        // When
        when(list).thenReturn(Page.empty());

        // Then
        mockMvc.perform(get("/api/main/study_group/board")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_게시글_리스트_조회시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given
        Page<StudyGroupBoardDto> list = studyGroupBoardService.studyGroupBoardList(any(), any());

        // When
        when(list).thenReturn(Page.empty());

        // Then
        mockMvc.perform(get("/api/main/study_group/board")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글_상세_조회_성공한_경우() throws Exception {
        // Given

        // When
        when(studyGroupBoardService.studyGroupBoardDetail(any()))
                .thenReturn(StudyGroupBoardDto.fromEntity(StudyGroupBoardFixture.get("userId", "title", "content")));

        // Then
        mockMvc.perform(get("/api/main/study_group/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 스터디_그룹_게시글_상세_조회시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given

        // When
        when(studyGroupBoardService.studyGroupBoardDetail(any()))
                .thenReturn(StudyGroupBoardDto.fromEntity(StudyGroupBoardFixture.get("userId", "title", "content")));

        // Then
        mockMvc.perform(get("/api/main/study_group/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 스터디_그룹_게시글_상세_조회시_게시글이_없는_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND))
                .when(studyGroupBoardService).studyGroupBoardDetail(any());

        // Then
        mockMvc.perform(get("/api/main/study_group/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }
}