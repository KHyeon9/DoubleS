package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.NoticeBoard;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.NoticeBoardFixture;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.repository.NoticeBoardRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class NoticeBoardServiceTest {

    @Autowired
    private NoticeBoardService noticeBoardService;

    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private NoticeBoardRepository noticeBoardRepository;

    @Test
    void 공지사항_작성이_성공한_경우() {
        // Given
        String title = "title";
        String content = "content";

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(noticeBoardRepository.save(any()))
                .thenReturn(mock(NoticeBoard.class));

        // Then
        assertDoesNotThrow(() -> noticeBoardService.createNoticeBoard(userId, title, content));
    }

    @Test
    void 공지사항_작성시_유저가_존재하지_않는_경우_에러_반환() {
        // Given
        String title = "title";
        String content = "content";

        String userId = "userId";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());
        when(noticeBoardRepository.save(any()))
                .thenReturn(mock(NoticeBoard.class));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> noticeBoardService.createNoticeBoard(userId, title, content)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 공지사항_작성시_관리자가_아닌_경우_에러_반환() {
        // Given
        String title = "title";
        String content = "content";

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.get(userId, password);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(noticeBoardRepository.save(any()))
                .thenReturn(mock(NoticeBoard.class));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> noticeBoardService.createNoticeBoard(userId, title, content)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 공지사항_수정이_성공한_경우() {
        // Given
        String title = "title";
        String content = "content";
        Long noticeBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);
        NoticeBoard noticeBoard = NoticeBoardFixture.get(userAccount);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(noticeBoardRepository.findById(noticeBoardId))
                .thenReturn(Optional.of(noticeBoard));
        when(noticeBoardRepository.saveAndFlush(any()))
                .thenReturn(noticeBoard);

        // Then
        assertDoesNotThrow(() -> noticeBoardService
                .modifyNoticeBoard(userId, noticeBoardId, title, content));
    }

    @Test
    void 공지사항_수정시_게시글이_없는_경우_에러_반환() {
        // Given
        String title = "title";
        String content = "content";
        Long noticeBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(noticeBoardRepository.findById(noticeBoardId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> noticeBoardService
                        .modifyNoticeBoard(userId, noticeBoardId, title, content)
        );
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }


    @Test
    void 공지사항_수정시_유저가_존재하지_않는_경우_에러_반환() {
        // Given
        String title = "title";
        String content = "content";
        Long noticeBoardId = 1L;

        String userId = "userId";

        UserAccount writer = UserAccountFixture.getAdmin("writer", "password");
        NoticeBoard noticeBoard = NoticeBoardFixture.get(writer);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());
        when(noticeBoardRepository.findById(noticeBoardId))
                .thenReturn(Optional.of(noticeBoard));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> noticeBoardService
                        .modifyNoticeBoard(userId, noticeBoardId, title, content)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 공지사항_수정시_유저가_다른_경우_에러_반환() {
        // Given
        String title = "title";
        String content = "content";
        Long noticeBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);
        UserAccount writer = UserAccountFixture.getAdmin("writer", "writer_password");
        NoticeBoard noticeBoard = NoticeBoardFixture.get(writer);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(noticeBoardRepository.findById(noticeBoardId))
                .thenReturn(Optional.of(noticeBoard));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> noticeBoardService
                        .modifyNoticeBoard(userId, noticeBoardId, title, content)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 공지사항_삭제가_성공한_경우() {
        // Given
        Long noticeBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);
        NoticeBoard noticeBoard = NoticeBoardFixture.get(userAccount);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(noticeBoardRepository.findById(noticeBoardId))
                .thenReturn(Optional.of(noticeBoard));

        // Then
        assertDoesNotThrow(() -> noticeBoardService
                .deleteNoticeBoard(userId, noticeBoardId));
    }

    @Test
    void 공지사항_삭제시_유저가_존재하지_않는_경우_에러_반환() {
        // Given
        Long noticeBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);
        NoticeBoard noticeBoard = NoticeBoardFixture.get(userAccount);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());
        when(noticeBoardRepository.findById(noticeBoardId))
                .thenReturn(Optional.of(noticeBoard));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> noticeBoardService
                        .deleteNoticeBoard(userId, noticeBoardId)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 공지사항_삭제시_게시글이_없는_경우_에러_반환() {
        // Given
        Long noticeBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(noticeBoardRepository.findById(noticeBoardId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> noticeBoardService
                        .deleteNoticeBoard(userId, noticeBoardId)
        );
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 공지사항_삭제시_유저가_다른_경우_에러_반환() {
        // Given
        Long noticeBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);
        UserAccount writer = UserAccountFixture.getAdmin("writer", "writer_password");
        NoticeBoard noticeBoard = NoticeBoardFixture.get(writer);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(noticeBoardRepository.findById(noticeBoardId))
                .thenReturn(Optional.of(noticeBoard));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> noticeBoardService
                        .deleteNoticeBoard(userId, noticeBoardId)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 공지사항_리스트_조회가_성공한_경우() {
        // Given
        Pageable pageable = mock(Pageable.class);

        // When
        when(noticeBoardRepository.findAll(pageable)).thenReturn(Page.empty());

        // Then
        assertDoesNotThrow(() -> noticeBoardService.noticeBoardList(pageable));
    }

    @Test
    void 공지사항_상세_조회_성공() {
        // Given
        Long noticeBoardId = 1L;
        String admin = "admin";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(admin, password);
        NoticeBoard noticeBoard = NoticeBoardFixture.get(userAccount);

        // When
        when(noticeBoardRepository.findById(noticeBoardId)).thenReturn(Optional.of(noticeBoard));

        // Then
        assertDoesNotThrow(() -> noticeBoardService.noticeBoardDetail(noticeBoardId));
    }

    @Test
    void 공지사항_상세_조회시_게시글이_없는_경우_에러_반환() {
        // Given
        Long noticeBoardId = 1L;

        // When
        when(noticeBoardRepository.findById(noticeBoardId)).thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> noticeBoardService.noticeBoardDetail(noticeBoardId)
        );

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }
}