package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.QuestionBoardRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
        assertDoesNotThrow(() -> questionBoardService.createPost(title, content, userId));
    }


    @Test
    void 질문_게시글_작성시_요청한_유저가_존재하지_않는_경우_에러_반환() {
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
                () -> questionBoardService.createPost(title, content, userId)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }
}