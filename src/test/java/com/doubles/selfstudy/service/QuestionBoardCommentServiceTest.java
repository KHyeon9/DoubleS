package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardComment;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.QuestionBoardCommentFixture;
import com.doubles.selfstudy.fixture.QuestionBoardFixture;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.repository.QuestionBoardCommentRepository;
import com.doubles.selfstudy.repository.QuestionBoardRepository;
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
class QuestionBoardCommentServiceTest {

    @Autowired
    private QuestionBoardCommentService questionBoardCommentService;

    @MockBean
    private QuestionBoardRepository questionBoardRepository;
    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private QuestionBoardCommentRepository questionBoardCommentRepository;

    @Test
    void 질문_게시글_댓글_생성_성공() {
        // Given
        String comment = "comment";
        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoard.getId())).thenReturn(Optional.of(questionBoard));
        when(questionBoardCommentRepository.save(any())).thenReturn(mock(QuestionBoardComment.class));

        // Then
        assertDoesNotThrow(() -> questionBoardCommentService.createQuestionBoardComment(userId, questionBoard.getId(), comment));
    }

    @Test
    void 질문_게시글_댓글_생성시_로그인하지_않은_경우_에러_반환() {
        // Given
        String comment = "comment";
        String userId = "userId";

        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());
        when(questionBoardRepository.findById(questionBoard.getId())).thenReturn(Optional.of(questionBoard));
        when(questionBoardCommentRepository.save(any())).thenReturn(mock(QuestionBoardComment.class));

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class,
                () -> questionBoardCommentService.createQuestionBoardComment(userId, questionBoard.getId(), comment)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 질문_게시글_댓글_생성시_게시글이_없는_경우_에러_반환() {
        // Given
        String comment = "comment";
        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.get(userId, password);

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(1L)).thenReturn(Optional.empty());
        when(questionBoardCommentRepository.save(any())).thenReturn(mock(QuestionBoardComment.class));

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class,
                () -> questionBoardCommentService.createQuestionBoardComment(userId, 1L, comment)
        );

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 질문_게시글_댓글_수정_성공() {
        // Given
        String modifyComment = "modify_comment";
        String userId = "userId";
        Long questionBoardId = 1L;
        Long questionBoardCommentId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get("boardWriter");
        QuestionBoardComment questionBoardComment = QuestionBoardCommentFixture.get(userId, questionBoard, "test");
        QuestionBoardComment modifyQuestionBoardComment = QuestionBoardCommentFixture.get(userId, questionBoard, modifyComment);
        UserAccount userAccount = questionBoardComment.getUserAccount();

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.of(questionBoard));
        when(questionBoardCommentRepository.findById(questionBoardCommentId)).thenReturn(Optional.of(questionBoardComment));
        when(questionBoardCommentRepository.saveAndFlush(any())).thenReturn(modifyQuestionBoardComment);

        // Then
        assertDoesNotThrow(() -> questionBoardCommentService.modifyQuestionBoardComment(userId, questionBoardId, questionBoardCommentId, modifyComment));
    }

    @Test
    void 질문_게시글_댓글_수정시_유저가_없는_경우_에러_반환() {
        // Given
        String comment = "comment";
        String userId = "userId";
        Long questionBoardId = 1L;
        Long questionBoardCommentId = 1L;


        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class, () ->
            questionBoardCommentService.modifyQuestionBoardComment(userId, questionBoardId, questionBoardCommentId, comment)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 질문_게시글_댓글_수정시_게시글이_없는_경우_에러_반환() {
        // Given
        String comment = "comment";
        String userId = "userId";
        Long questionBoardId = 1L;
        Long questionBoardCommentId = 1L;

        UserAccount userAccount = UserAccountFixture.get(userId, "password");

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class, () ->
                questionBoardCommentService.modifyQuestionBoardComment(userId, questionBoardId, questionBoardCommentId, comment)
        );

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 질문_게시글_댓글_수정시_권한이_없는_경우_에러_반환() {
        // Given
        String comment = "comment";
        String userId = "userId";
        Long questionBoardId = 1L;
        Long questionBoardCommentId = 1L;

        UserAccount modifyUser = UserAccountFixture.get(userId, "password");
        QuestionBoard questionBoard = QuestionBoardFixture.get("boardWriter");
        QuestionBoardComment questionBoardComment = QuestionBoardCommentFixture.get("testUserID", questionBoard, "test");

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(modifyUser));
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.of(questionBoard));
        when(questionBoardCommentRepository.findById(questionBoardCommentId)).thenReturn(Optional.of(questionBoardComment));

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class, () ->
                questionBoardCommentService.modifyQuestionBoardComment(userId, questionBoardId, questionBoardCommentId, comment)
        );

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 질문_게시글_댓글_삭제_성공() {
        // Given
        String userId = "userId";
        Long questionBoardCommentId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get("boardWriter");
        QuestionBoardComment questionBoardComment = QuestionBoardCommentFixture.get(userId, questionBoard, "test");
        UserAccount userAccount = questionBoardComment.getUserAccount();

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoard.getId())).thenReturn(Optional.of(questionBoard));
        when(questionBoardCommentRepository.findById(questionBoardCommentId)).thenReturn(Optional.of(questionBoardComment));
        when(questionBoardCommentRepository.saveAndFlush(any())).thenReturn(questionBoardComment);

        // Then
        assertDoesNotThrow(() -> questionBoardCommentService
                .deleteQuestionBoardComment(userId, questionBoard.getId(), questionBoardCommentId));
    }

    @Test
    void 질문_게시글_댓글_삭제시_유저가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long questionBoardCommentId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get("boardWriter");
        QuestionBoardComment questionBoardComment = QuestionBoardCommentFixture.get("testUserId", questionBoard, "test");

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());
        when(questionBoardCommentRepository.findById(questionBoardCommentId)).thenReturn(Optional.of(questionBoardComment));
        when(questionBoardCommentRepository.saveAndFlush(any())).thenReturn(questionBoardComment);

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class, () ->
                questionBoardCommentService.deleteQuestionBoardComment(userId, questionBoard.getId(), questionBoardCommentId)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 질문_게시글_댓글_삭제시_질문_게시글이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long questionBoardCommentId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get("boardWriter");
        QuestionBoardComment questionBoardComment = QuestionBoardCommentFixture.get("testUserId", questionBoard, "test");
        UserAccount userAccount = questionBoardComment.getUserAccount();

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoard.getId())).thenReturn(Optional.empty());
        when(questionBoardCommentRepository.findById(questionBoardCommentId)).thenReturn(Optional.of(questionBoardComment));
        when(questionBoardCommentRepository.saveAndFlush(any())).thenReturn(questionBoardComment);

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class, () ->
                questionBoardCommentService.deleteQuestionBoardComment(userId, questionBoard.getId(), questionBoardCommentId)
        );

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 질문_게시글_댓글_삭제시_댓글이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long questionBoardCommentId = 1L;

        UserAccount userAccount = UserAccountFixture.get(userId, "password");
        QuestionBoard questionBoard = QuestionBoardFixture.get("boardWriter");

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoard.getId())).thenReturn(Optional.of(questionBoard));
        when(questionBoardCommentRepository.findById(questionBoardCommentId)).thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class, () ->
                questionBoardCommentService.deleteQuestionBoardComment(userId, questionBoard.getId(), questionBoardCommentId)
        );

        assertEquals(ErrorCode.COMMENT_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 질문_게시글_댓글_삭제시_권한이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long questionBoardCommentId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get("boardWriter");
        QuestionBoardComment questionBoardComment = QuestionBoardCommentFixture.get("testUserId", questionBoard, "test");
        UserAccount deleteUser = UserAccountFixture.get(userId, "password");

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(deleteUser));
        when(questionBoardRepository.findById(questionBoard.getId())).thenReturn(Optional.of(questionBoard));
        when(questionBoardCommentRepository.findById(questionBoardCommentId)).thenReturn(Optional.of(questionBoardComment));

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class, () ->
                questionBoardCommentService.deleteQuestionBoardComment(userId, questionBoard.getId(), questionBoardCommentId)
        );

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 질문_게시글_댓글_리스트_조회_성공() {
        // Given
        Pageable pageable = mock(Pageable.class);
        Long qustionBoardId = 1L;
        QuestionBoard questionBoard = mock(QuestionBoard.class);

        // When
        when(questionBoardRepository.findById(qustionBoardId)).thenReturn(Optional.of(questionBoard));
        when(questionBoardCommentRepository.findAllByQuestionBoard(questionBoard, pageable))
                .thenReturn(Page.empty());

        // Then
        assertDoesNotThrow(() -> questionBoardCommentService.questionBoardCommentList(qustionBoardId, pageable));
    }
}