package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.question.QuestionBoardTag;
import com.doubles.selfstudy.entity.Alarm;
import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardLike;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.QuestionBoardFixture;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
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
    @MockBean
    private QuestionBoardCommentRepository questionBoardCommentRepository;
    @MockBean
    private AlarmRepository alarmRepository;

    @Test
    void 질문_게시글_작성이_성공한_경우() {
        // Given
        String title = "title";
        String content = "content";
        String tag = "Free";
        String userId = "userId";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(questionBoardRepository.save(any())).thenReturn(mock(QuestionBoard.class));

        // Then
        assertDoesNotThrow(() -> questionBoardService.createQuestionBoard(userId, title, content, tag));
    }


    @Test
    void 질문_게시글_작성시_로그인한_유저가_존재하지_않는_경우_에러_반환() {
        // Given
        String title = "title";
        String content = "content";
        String tag = "Free";
        String userId = "userId";

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());
        when(questionBoardRepository.save(any())).thenReturn(mock(QuestionBoard.class));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> questionBoardService.createQuestionBoard(userId, title, content, tag)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 질문_게시글_수정이_성공한_경우() {
        // Given
        String modifiyTitle = "modifiy_title";
        String modifiyContent = "modifiy_content";
        String tag = "Free";
        String userId = "userId";
        Long questionBoardId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);
        QuestionBoard modifyQuestionBoard = QuestionBoardFixture.get(userId, modifiyTitle, modifiyContent);
        UserAccount userAccount = questionBoard.getUserAccount();

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.of(questionBoard));
        when(questionBoardRepository.saveAndFlush(any())).thenReturn(modifyQuestionBoard);

        // Then
        assertDoesNotThrow(() -> questionBoardService.modifyQuestionBoard(userId, questionBoardId, modifiyTitle, modifiyContent, tag));
    }

    @Test
    void 질문_게시글_수정시_게시글이_존재하지_않을_경우_에러_반환() {
        // Given
        String modifiyTitle = "modifiy_title";
        String modifiyContent = "modifiy_content";
        String tag = "Free";
        String userId = "userId";
        Long questionBoardId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);
        UserAccount userAccount = questionBoard.getUserAccount();

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class, () ->
                    questionBoardService.modifyQuestionBoard(userId, questionBoardId, modifiyTitle, modifiyContent, tag)
                );

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 질문_게시글_수정시_권한이_없는_경우_에러_반환() {
        // Given
        String title = "modifiy_title";
        String content = "modifiy_content";
        String tag = "Free";
        String userId = "userId";
        Long questionBoardId = 1L;

        QuestionBoard questionBoard = QuestionBoardFixture.get("testUserId");
        UserAccount writer = UserAccountFixture.get(userId, "password");

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(writer));
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.of(questionBoard));

        // Then
        DoubleSApplicationException e = assertThrows(DoubleSApplicationException.class, () ->
                questionBoardService.modifyQuestionBoard(userId, questionBoardId, title, content, tag)
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
        when(questionBoardRepository.findAllQuestionBoardWithCounts(pageable)).thenReturn(Page.empty());

        // Then
        assertDoesNotThrow(() -> questionBoardService.questionBoardList(pageable));
    }

    @Test
    void 태그를_통한_질문_게시글_리스트_조회가_성공한_경우() {
        // Given
        Pageable pageable = mock(Pageable.class);
        QuestionBoardTag tag = QuestionBoardTag.Free;

        // When
        when(questionBoardRepository.findAllQuestionBoardWithCountsByTag(tag, pageable)).thenReturn(Page.empty());

        // Then
        assertDoesNotThrow(() -> questionBoardService.questionBoardListByTag(tag.name(), pageable));
    }

    @Test
    void 내_게시글_목록_조회가_성공한_경우() {
        // Given
        Pageable pageable = mock(Pageable.class);
        UserAccount userAccount = mock(UserAccount.class);

        // When
        when(userAccountRepository.findById(any())).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findAllMyQuestionBoardWithCounts(userAccount.getUserId(), pageable)).thenReturn(Page.empty());

        // Then
        assertDoesNotThrow(() -> questionBoardService.myQuestionBoardList(userAccount.getUserId(), pageable));
    }

    @Test
    void 태그를_통해_내_게시글_목록_조회가_성공한_경우() {
        // Given
        Pageable pageable = mock(Pageable.class);
        UserAccount userAccount = mock(UserAccount.class);
        QuestionBoardTag tag = QuestionBoardTag.Free;

        // When
        when(userAccountRepository.findById(any())).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findAllMyQuestionBoardWithCountsByTag(userAccount.getUserId(), tag, pageable)).thenReturn(Page.empty());

        // Then
        assertDoesNotThrow(() -> questionBoardService.myQuestionBoardListByTag(userAccount.getUserId(), tag.name(), pageable));
    }

    @Test
    void 회원_정보_페이지에_질문_게시글_리스트_조회_성공() {
        // Given
        UserAccount userAccount = mock(UserAccount.class);

        // When
        when(userAccountRepository.findById(any())).thenReturn(Optional.of(userAccount));
        when(questionBoardRepository.findTop4ByUserAccountOrderByCreatedAtDesc(any())).thenReturn(List.of());

        // Then
        assertDoesNotThrow(() -> questionBoardService.profileQuestionBoardList(userAccount.getUserId()));
    }

    @Test
    void 질문_게시글_상세_조회가_성공한_경우() {
        // Given
        String userId = "userId";
        Long questionBoardId = 1L;
        Long likes = 1L;
        Long comments = 0L;

        QuestionBoard questionBoard = QuestionBoardFixture.get(userId);
        questionBoard.plusViewCount();

        // When
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.of(questionBoard));
        when(questionBoardRepository.save(questionBoard)).thenReturn(questionBoard);
        when(questionBoardLikeRepository.countByQuestionBoard(questionBoard)).thenReturn(likes);
        when(questionBoardCommentRepository.countByQuestionBoard(questionBoard)).thenReturn(comments);

        // Then
        assertDoesNotThrow(() -> questionBoardService.questionBoardDetail(questionBoardId));
    }

    @Test
    void 질문_게시글_상세_조회시_게시글이_없는_경우_에러_반환() {
        // Given
        Long questionBoardId = 1L;

        // When
        when(questionBoardRepository.findById(questionBoardId)).thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> questionBoardService.questionBoardDetail(questionBoardId)
        );

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
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
        when(alarmRepository.save(any()))
                .thenReturn(mock(Alarm.class));

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