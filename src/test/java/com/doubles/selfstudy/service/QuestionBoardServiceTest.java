package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardLike;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.QuestionBoardFixture;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.repository.QuestionBoardLikeRepository;
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
import static org.mockito.Mockito.*;

@SpringBootTest
class QuestionBoardServiceTest {

    @Autowired
    private QuestionBoardService questionBoardService;

    @MockBean
    private QuestionBoardRepository questionBoardRepository;
    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private QuestionBoardLikeRepository questionBoardLikeRepository;

    @Test
    void 질문_게시글_작성이_성공한_경우() {
        // Given
        String title = "title";
        String content = "content";
        String userId = "userId";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(questionBoardRepository.save(any())).thenReturn(mock(QuestionBoard.class));

        // Then
        assertDoesNotThrow(() -> questionBoardService.createQuestionBoard(title, content, userId));
    }


    @Test
    void 질문_게시글_작성시_로그인한_유저가_존재하지_않는_경우_에러_반환() {
        // Given
        String title = "title";
        String content = "content";
        String userId = "userId";

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());
        when(questionBoardRepository.save(any())).thenReturn(mock(QuestionBoard.class));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> questionBoardService.createQuestionBoard(title, content, userId)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 질문_게시글_수정이_성공한_경우() {
        // Given
        String title = "modifiy_title";
        String content = "modifiy_content";
        String userId = "userId";
        Long questionBoardId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);
        UserAccount userAccount = questionBoard.getUserAccount();
        when(questionBoardRepository.saveAndFlush(any())).thenReturn(questionBoard);

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.of(questionBoard));

        // Then
        assertDoesNotThrow(() -> questionBoardService.modifyQuestionBoard(title, content, userId, questionBoardId));
    }

    @Test
    void 질문_게시글_수정시_게시글이_존재하지_않을_경우_에러_반환() {
        // Given
        String title = "modifiy_title";
        String content = "modifiy_content";
        String userId = "userId";
        Long questionBoardId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);
        UserAccount userAccount = questionBoard.getUserAccount();

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class, () ->
                    questionBoardService.modifyQuestionBoard(title, content, userId, questionBoardId)
                );

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 질문_게시글_수정시_권한이_없는_경우_에러_반환() {
        // Given
        String title = "modifiy_title";
        String content = "modifiy_content";
        String userId = "userId";
        Long questionBoardId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);
        UserAccount writer = UserAccountFixture.get("testUserId", "password");

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(writer));
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.of(questionBoard));

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class, () ->
                questionBoardService.modifyQuestionBoard(title, content, userId, questionBoardId)
        );

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 질문_게시글_삭제_성공한_경우() {
        // Given
        String userId = "userId";
        Long questionBoardId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);
        UserAccount userAccount = questionBoard.getUserAccount();

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.of(questionBoard));

        // Then
        assertDoesNotThrow(() -> questionBoardService.deleteQuestionBoard(userId, questionBoardId));
    }

    @Test
    void 질문_게시글_삭제시_포스트가_존재하지_않는_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long questionBoardId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);
        UserAccount userAccount = questionBoard.getUserAccount();

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> questionBoardService.deleteQuestionBoard(userId, questionBoardId)
        );

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 질문_게시글_삭제시_권한이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long questionBoardId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);
        UserAccount writer = UserAccountFixture.get("writerUser", "password");

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(writer));
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.of(questionBoard));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> questionBoardService.deleteQuestionBoard(userId, questionBoardId)
        );

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 질문_게시글_리스트_조회가_성공한_경우() {
        // Given
        Pageable pageable = mock(Pageable.class);

        // When
        when(questionBoardRepository.findAllByWithLikeCountAndCommentCount(pageable)).thenReturn(Page.empty());

        // Then
        assertDoesNotThrow(() -> questionBoardService.questionBoardList(pageable));
    }

    @Test
    void 내_게시글_목록_조회가_성공한_경우() {
        // Given
        Pageable pageable = mock(Pageable.class);
        UserAccount userAccount = mock(UserAccount.class);

        // When
        when(userAccountRepository.findById(any())).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findByUserAccount(userAccount, pageable)).thenReturn(Page.empty());

        // Then
        assertDoesNotThrow(() -> questionBoardService.myQuestionBoardList("", pageable));
    }

    @Test
    void 게시글_좋아요가_성공한_경우() {
        // Given
        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);


        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoard.getId())).thenReturn(Optional.of(questionBoard));

        // Then
        assertDoesNotThrow(() -> questionBoardService.questionBoardLike(userId, questionBoard.getId()));
    }

    @Test
    void 게시글_좋아요시_게시글이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);


        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoard.getId())).thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> questionBoardService.questionBoardLike(userId, questionBoard.getId())
        );

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 게시글_좋아요가_이미_있는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);
        QuestionBoardLike questionBoardLike = QuestionBoardLike.of(questionBoard, userAccount);


        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoard.getId())).thenReturn(Optional.of(questionBoard));
        when(questionBoardLikeRepository.findByUserAccountAndQuestionBoard(userAccount, questionBoard))
                .thenReturn(Optional.of(questionBoardLike));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> questionBoardService.questionBoardLike(userId, questionBoard.getId())
        );

        assertEquals(ErrorCode.ALREADY_LIKED, e.getErrorCode());
    }


}