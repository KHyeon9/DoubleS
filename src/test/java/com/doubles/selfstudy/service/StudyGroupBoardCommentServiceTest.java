package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.Alarm;
import com.doubles.selfstudy.entity.StudyGroupBoard;
import com.doubles.selfstudy.entity.StudyGroupBoardComment;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.StudyGroupBoardCommentFixture;
import com.doubles.selfstudy.fixture.StudyGroupBoardFixture;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
class StudyGroupBoardCommentServiceTest {

    @Autowired
    private StudyGroupBoardCommentService studyGroupBoardCommentService;

    @MockBean
    private StudyGroupBoardCommentRepository studyGroupBoardCommentRepository;
    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private UserAccountCacheRepository userAccountCacheRepository;
    @MockBean
    private StudyGroupBoardRepository studyGroupBoardRepository;
    @MockBean
    private AlarmRepository alarmRepository;
    @MockBean
    private AlarmService alarmService;

    @Test
    void 스터디_그룹_게시글의_댓글_생성이_성공한_경우() {
        // Given
        String userId = "userId";
        Long studyGroupBoardId = 1L;
        String comment = "comment";

        UserAccount userAccount = UserAccountFixture.get(userId, "password");
        StudyGroupBoard studyGroupBoard = StudyGroupBoardFixture.get("writer", "title", "content");

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardRepository.findById(studyGroupBoardId))
                .thenReturn(Optional.of(studyGroupBoard));
        when(alarmRepository.save(any()))
                .thenReturn(mock(Alarm.class));

        // Then
        assertDoesNotThrow(() -> studyGroupBoardCommentService
                .createStudyGroupBoardCommnet(
                        userId,
                        studyGroupBoardId,
                        comment
                )
        );
    }

    @Test
    void 스터디_그룹_게시글의_댓글_생성시_로그인을_안한_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long studyGroupBoardId = 1L;
        String comment = "comment";

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class,
                () -> studyGroupBoardCommentService.createStudyGroupBoardCommnet(userId, studyGroupBoardId, comment)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글의_댓글_생성시_게시글을_못찾은_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long studyGroupBoardId = 1L;
        String comment = "comment";

        UserAccount userAccount = UserAccountFixture.get(userId, "password");

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardRepository.findById(studyGroupBoardId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class,
                () -> studyGroupBoardCommentService.createStudyGroupBoardCommnet(userId, studyGroupBoardId, comment)
        );

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글의_댓글_수정이_성공한_경우() {
        // Given
        String userId = "userId";
        String password = "password";
        Long studyGroupBoardCommentId = 1L;
        String comment = "modify_comment";

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        StudyGroupBoardComment studyGroupBoardComment =
                StudyGroupBoardCommentFixture.get(userAccount, "comment");
        StudyGroupBoardComment modifyStudyGroupBoardComment =
                StudyGroupBoardCommentFixture.get(userAccount, comment);


        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardCommentRepository.findById(studyGroupBoardCommentId))
                .thenReturn(Optional.of(studyGroupBoardComment));
        when(studyGroupBoardCommentRepository.saveAndFlush(any()))
                .thenReturn(modifyStudyGroupBoardComment);

        // Then
        assertDoesNotThrow(() -> studyGroupBoardCommentService
                .modifyStudyGroupBoardComment(
                        userId,
                        studyGroupBoardCommentId,
                        comment
                )
        );
    }

    @Test
    void 스터디_그룹_게시글의_댓글_수정시_로그인을_안한_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long studyGroupBoardCommentId = 1L;
        String comment = "modify_comment";

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class,
                () -> studyGroupBoardCommentService
                        .modifyStudyGroupBoardComment(userId, studyGroupBoardCommentId, comment)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글의_댓글_수정시_댓글이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";
        Long studyGroupBoardCommentId = 1L;
        String comment = "modify_comment";

        UserAccount userAccount = UserAccountFixture.get(userId, password);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardCommentRepository.findById(studyGroupBoardCommentId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class,
                () -> studyGroupBoardCommentService
                        .modifyStudyGroupBoardComment(userId, studyGroupBoardCommentId, comment)
        );

        assertEquals(ErrorCode.COMMENT_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글의_댓글_수정시_작성자가_아닌_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";
        Long studyGroupBoardCommentId = 1L;
        String comment = "modify_comment";

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        UserAccount writer = UserAccountFixture.get("writer", "writer_password");
        StudyGroupBoardComment studyGroupBoardComment =
                StudyGroupBoardCommentFixture.get(writer, "comment");
        StudyGroupBoardComment modifyStudyGroupBoardComment =
                StudyGroupBoardCommentFixture.get(userAccount, comment);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardCommentRepository.findById(studyGroupBoardCommentId))
                .thenReturn(Optional.of(studyGroupBoardComment));
        when(studyGroupBoardCommentRepository.saveAndFlush(any()))
                .thenReturn(modifyStudyGroupBoardComment);

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class,
                () -> studyGroupBoardCommentService
                        .modifyStudyGroupBoardComment(userId, studyGroupBoardCommentId, comment)
        );

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글의_댓글_삭제가_성공한_경우() {
        // Given
        String userId = "userId";
        String password = "password";
        Long studyGroupBoardCommentId = 1L;

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        StudyGroupBoardComment studyGroupBoardComment =
                StudyGroupBoardCommentFixture.get(userAccount, "comment");

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardCommentRepository.findById(studyGroupBoardCommentId))
                .thenReturn(Optional.of(studyGroupBoardComment));

        // Then
        assertDoesNotThrow(() ->
                studyGroupBoardCommentService
                        .deleteStudyGroupBoardComment(userId, studyGroupBoardCommentId));
    }

    @Test
    void 스터디_그룹_게시글의_댓글_삭제시_로그인하지_않은_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long studyGroupBoardCommentId = 1L;

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class,
                () -> studyGroupBoardCommentService
                        .deleteStudyGroupBoardComment(userId, studyGroupBoardCommentId)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글의_댓글_삭제시_댓글이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";
        Long studyGroupBoardCommentId = 1L;

        UserAccount userAccount = UserAccountFixture.get(userId, password);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardCommentRepository.findById(studyGroupBoardCommentId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class,
                () -> studyGroupBoardCommentService
                        .deleteStudyGroupBoardComment(userId, studyGroupBoardCommentId)
        );

        assertEquals(ErrorCode.COMMENT_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글의_댓글_삭제시_작성자가_다른_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";
        Long studyGroupBoardCommentId = 1L;

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        UserAccount writer = UserAccountFixture.get("writer", "writer_password");
        StudyGroupBoardComment studyGroupBoardComment =
                StudyGroupBoardCommentFixture.get(writer, "comment");

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardCommentRepository.findById(studyGroupBoardCommentId))
                .thenReturn(Optional.of(studyGroupBoardComment));

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class,
                () -> studyGroupBoardCommentService
                        .deleteStudyGroupBoardComment(userId, studyGroupBoardCommentId)
        );

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글의_댓글_리스트_조회_성공() {
        // Given
        Pageable pageable = mock(Pageable.class);
        Long studyGroupBoardId = 1L;
        StudyGroupBoard studyGroupBoard = mock(StudyGroupBoard.class);

        // When
        when(studyGroupBoardRepository.findById(studyGroupBoardId))
                .thenReturn(Optional.of(studyGroupBoard));
        when(studyGroupBoardCommentRepository.findAllByStudyGroupBoard(studyGroupBoard, pageable))
                .thenReturn(Page.empty());

        // Then
        assertDoesNotThrow(() ->
                studyGroupBoardCommentService
                        .studyGroupBoardCommentList(studyGroupBoardId, pageable)
        );
    }
}