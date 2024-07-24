package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardComment;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.QuestionBoardFixture;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.repository.QuestionBoardCommentRepository;
import com.doubles.selfstudy.repository.QuestionBoardLikeRepository;
import com.doubles.selfstudy.repository.QuestionBoardRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
}