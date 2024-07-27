package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.NotificationBoard;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.NotificationBoardFixture;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.repository.NotificationBoardRepository;
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
class NotificationBoardServiceTest {

    @Autowired
    private NotificationBoardService notificationBoardService;

    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private NotificationBoardRepository notificationBoardRepository;

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
        when(notificationBoardRepository.save(any()))
                .thenReturn(mock(NotificationBoard.class));

        // Then
        assertDoesNotThrow(() -> notificationBoardService.createNofificationBoard(userId, title, content));
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
        when(notificationBoardRepository.save(any()))
                .thenReturn(mock(NotificationBoard.class));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> notificationBoardService.createNofificationBoard(userId, title, content)
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
        when(notificationBoardRepository.save(any()))
                .thenReturn(mock(NotificationBoard.class));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> notificationBoardService.createNofificationBoard(userId, title, content)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 공지사항_수정이_성공한_경우() {
        // Given
        String title = "title";
        String content = "content";
        Long notificationBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);
        NotificationBoard notificationBoard = NotificationBoardFixture.get(userAccount);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(notificationBoardRepository.findById(notificationBoardId))
                .thenReturn(Optional.of(notificationBoard));
        when(notificationBoardRepository.saveAndFlush(any()))
                .thenReturn(notificationBoard);

        // Then
        assertDoesNotThrow(() -> notificationBoardService
                .updateNofificationBoard(userId, notificationBoardId, title, content));
    }

    @Test
    void 공지사항_수정시_게시글이_없는_경우_에러_반환() {
        // Given
        String title = "title";
        String content = "content";
        Long notificationBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(notificationBoardRepository.findById(notificationBoardId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> notificationBoardService
                        .updateNofificationBoard(userId, notificationBoardId, title, content)
        );
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }


    @Test
    void 공지사항_수정시_유저가_존재하지_않는_경우_에러_반환() {
        // Given
        String title = "title";
        String content = "content";
        Long notificationBoardId = 1L;

        String userId = "userId";

        UserAccount writer = UserAccountFixture.getAdmin("writer", "password");
        NotificationBoard notificationBoard = NotificationBoardFixture.get(writer);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());
        when(notificationBoardRepository.findById(notificationBoardId))
                .thenReturn(Optional.of(notificationBoard));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> notificationBoardService
                        .updateNofificationBoard(userId, notificationBoardId, title, content)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 공지사항_수정시_유저가_다른_경우_에러_반환() {
        // Given
        String title = "title";
        String content = "content";
        Long notificationBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);
        UserAccount writer = UserAccountFixture.getAdmin("writer", "writer_password");
        NotificationBoard notificationBoard = NotificationBoardFixture.get(writer);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(notificationBoardRepository.findById(notificationBoardId))
                .thenReturn(Optional.of(notificationBoard));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> notificationBoardService
                        .updateNofificationBoard(userId, notificationBoardId, title, content)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 공지사항_삭제가_성공한_경우() {
        // Given
        Long notificationBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);
        NotificationBoard notificationBoard = NotificationBoardFixture.get(userAccount);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(notificationBoardRepository.findById(notificationBoardId))
                .thenReturn(Optional.of(notificationBoard));

        // Then
        assertDoesNotThrow(() -> notificationBoardService
                .deleteNofificationBoard(userId, notificationBoardId));
    }

    @Test
    void 공지사항_삭제시_유저가_존재하지_않는_경우_에러_반환() {
        // Given
        Long notificationBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);
        NotificationBoard notificationBoard = NotificationBoardFixture.get(userAccount);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());
        when(notificationBoardRepository.findById(notificationBoardId))
                .thenReturn(Optional.of(notificationBoard));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> notificationBoardService
                        .deleteNofificationBoard(userId, notificationBoardId)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 공지사항_삭제시_게시글이_없는_경우_에러_반환() {
        // Given
        Long notificationBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(notificationBoardRepository.findById(notificationBoardId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> notificationBoardService
                        .deleteNofificationBoard(userId, notificationBoardId)
        );
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 공지사항_삭제시_유저가_다른_경우_에러_반환() {
        // Given
        Long notificationBoardId = 1L;

        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.getAdmin(userId, password);
        UserAccount writer = UserAccountFixture.getAdmin("writer", "writer_password");
        NotificationBoard notificationBoard = NotificationBoardFixture.get(writer);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(notificationBoardRepository.findById(notificationBoardId))
                .thenReturn(Optional.of(notificationBoard));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> notificationBoardService
                        .deleteNofificationBoard(userId, notificationBoardId)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 공지사항_리스트_조회가_성공한_경우() {
        // Given
        Pageable pageable = mock(Pageable.class);

        // When
        when(notificationBoardRepository.findAll(pageable)).thenReturn(Page.empty());

        // Then
        assertDoesNotThrow(() -> notificationBoardService.nofificationBoardList(pageable));
    }
}