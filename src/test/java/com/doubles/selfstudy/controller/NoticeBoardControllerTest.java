package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.NoticeBoardRequest;
import com.doubles.selfstudy.dto.notice.NoticeBoardDto;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.NoticeBoardFixture;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.service.NoticeBoardService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class NoticeBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoticeBoardService noticeBoardService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 공지사항_게시글_작성_성공() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When&Then
        mockMvc.perform(post("/api/main/notice_board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new NoticeBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 공지사항_게시글_작성시_관리자가_아닌_경우_에러_발생() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When&Then
        mockMvc.perform(post("/api/main/notice_board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new NoticeBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.ACCESS_DENIED.getStatus().value()));
    }

    @Test
    @WithAnonymousUser
    void 공지사항_작성시_유저가_없는_경우_에러_발생() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When&Then
        mockMvc.perform(post("/api/main/notice_board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new NoticeBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 공지사항_수정이_성공한_경우() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When
        when(noticeBoardService.modifyNoticeBoard(any(), eq(1L), eq(title), eq(content)))
                .thenReturn(NoticeBoardDto.fromEntity(NoticeBoardFixture.get(
                            UserAccountFixture.getAdmin("admin", "password"))
                        )
                );

        // Then
        mockMvc.perform(put("/api/main/notice_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new NoticeBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 공지사항_수정시_본인이_작성한_글이_아닌_경우_에러_발생() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(noticeBoardService).modifyNoticeBoard(any(), eq(1L), eq(title), eq(content));

        // Then
        mockMvc.perform(put("/api/main/notice_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new NoticeBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 공지사항_수정시_관리자가_아닌_경우_에러_발생() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When
        when(noticeBoardService.modifyNoticeBoard(any(), eq(1L), eq(title), eq(content)))
                .thenReturn(NoticeBoardDto.fromEntity(NoticeBoardFixture.get(
                                UserAccountFixture.getAdmin("admin", "password"))
                        )
                );

        // Then
        mockMvc.perform(put("/api/main/notice_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new NoticeBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.ACCESS_DENIED.getStatus().value()));
    }

    @Test
    @WithAnonymousUser
    void 공지사항_수정시_유저가_없는_경우_에러_발생() throws Exception {
        // Given
        String title = "title";
        String content = "content";

        // When
        when(noticeBoardService.modifyNoticeBoard(any(), eq(1L), eq(title), eq(content)))
                .thenReturn(NoticeBoardDto.fromEntity(NoticeBoardFixture.get(
                                UserAccountFixture.getAdmin("admin", "password"))
                        )
                );

        // Then
        mockMvc.perform(put("/api/main/notice_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new NoticeBoardRequest(title, content)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 공지사항_삭제가_성공한_경우() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/notice_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 공지사항_삭제시_작성한_유저가_아닌_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(noticeBoardService).deleteNoticeBoard(any(), any());

        // Then
        mockMvc.perform(delete("/api/main/notice_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 공지사항_삭제시_관리자가_아닌_경우_에러_발생() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/notice_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.ACCESS_DENIED.getStatus().value()));
    }

    @Test
    @WithAnonymousUser
    void 공지사항_삭제시_유저가_없는_경우_에러_발생() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/notice_board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 공지사항_목록_조회_성공() throws Exception {
        // Given
        Page<NoticeBoardDto> list = noticeBoardService.noticeBoardList(any());

        // When
        when(list).thenReturn(Page.empty());

        // Then
        mockMvc.perform(get("/api/main/notice_board")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 공지사항_목록_조회시_로그인_안한_경우_에러_발생() throws Exception {
        // Given
        Page<NoticeBoardDto> list = noticeBoardService.noticeBoardList(any());

        // When
        when(list).thenReturn(Page.empty());

        // Then
        mockMvc.perform(get("/api/main/notice_board")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }
}