package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.Todo;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.TodoFixture;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.repository.TodoRepository;
import com.doubles.selfstudy.repository.UserAccountCacheRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private UserAccountCacheRepository userAccountCacheRepository;
    @MockBean
    private TodoRepository todoRepository;

    @Test
    void todo_생성이_성공한_경우() {
        // Given
        String userId = "userId";
        String content = "content";
        String importanceType = "Middle";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(todoRepository.save(any())).thenReturn(mock(Todo.class));

        // Then
        assertDoesNotThrow(() -> todoService.createTodo(userId, content, importanceType));
    }

    @Test
    void todo_생성시_유저가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String content = "content";
        String importanceType = "Middle";

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());
        when(todoRepository.save(any())).thenReturn(mock(Todo.class));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> todoService.createTodo(userId, content, importanceType)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void todo의_완료_상태_변환_성공한_경우() {
        // Given
        String userId = "userId";
        Long todoId = 1L;

        Todo todo = TodoFixture.get(userId);
        Todo modifyTodo = TodoFixture.get(userId, true);
        UserAccount userAccount = todo.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(todoRepository.findById(todoId))
                .thenReturn(Optional.of(todo));
        when(todoRepository.saveAndFlush(any())).thenReturn(modifyTodo);

        // Then
        assertDoesNotThrow(() -> todoService.changeTodoCompletedStatus(userId, todoId));
    }

    @Test
    void todo의_완료_상태_변환시_유저가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long todoId = 1L;

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> todoService.changeTodoCompletedStatus(userId, todoId)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void todo의_완료_상태_변환시_todo가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long todoId = 1L;

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(todoRepository.findById(todoId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> todoService.changeTodoCompletedStatus(userId, todoId)
        );

        assertEquals(ErrorCode.TODO_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void todo의_완료_상태_변환시_유저가_다른_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long todoId = 1L;

        Todo todo = TodoFixture.get("testUserId");
        UserAccount writer = UserAccountFixture.get(userId, "password");

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(writer));
        when(todoRepository.findById(todoId))
                .thenReturn(Optional.of(todo));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> todoService.changeTodoCompletedStatus(userId, todoId)
        );

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void todo의_정보_변환_성공한_경우() {
        // Given
        String userId = "userId";
        Long todoId = 1L;
        String content = "modify_comment";
        String importanceType = "High";

        Todo todo = TodoFixture.get(userId);
        Todo modifyTodo = TodoFixture.get(userId, content, importanceType);
        UserAccount userAccount = todo.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(todoRepository.findById(todoId))
                .thenReturn(Optional.of(todo));
        when(todoRepository.saveAndFlush(any())).thenReturn(modifyTodo);

        // Then
        assertDoesNotThrow(() -> todoService.modifyTodoInfo(userId, todoId, content, importanceType));
    }

    @Test
    void todo의_정보_변환시_유저가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long todoId = 1L;
        String content = "modify_comment";
        String importanceType = "High";


        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> todoService.modifyTodoInfo(userId, todoId, content, importanceType)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void todo의_정보_변환시_todo가_존재하지_않는_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long todoId = 1L;
        String content = "modify_comment";
        String importanceType = "High";

        Todo todo = TodoFixture.get(userId);
        UserAccount userAccount = todo.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(todoRepository.findById(todoId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> todoService.modifyTodoInfo(userId, todoId, content, importanceType)
        );

        assertEquals(ErrorCode.TODO_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void todo의_정보_변환시_유저가_다른_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long todoId = 1L;
        String content = "modify_comment";
        String importanceType = "High";

        Todo todo = TodoFixture.get("testUserId");
        UserAccount writer = UserAccountFixture.get(userId, "password");

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(writer));
        when(todoRepository.findById(todoId))
                .thenReturn(Optional.of(todo));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> todoService.modifyTodoInfo(userId, todoId, content, importanceType)
        );

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void todo의_삭제가_성공한_경우() {
        // Given
        String userId = "userId";
        Long todoId = 1L;

        Todo todo = TodoFixture.get(userId);
        UserAccount userAccount = todo.getUserAccount();


        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(todoRepository.findById(todoId))
                .thenReturn(Optional.of(todo));

        // Then
        assertDoesNotThrow(() -> todoService.deleteTodo(userId, todoId));
    }

    @Test
    void todo의_삭제시_유저가_존재하지_않는_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long todoId = 1L;

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> todoService.deleteTodo(userId, todoId)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void todo의_삭제시_todo가_존재하지_않는_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long todoId = 1L;

        Todo todo = TodoFixture.get(userId);
        UserAccount userAccount = todo.getUserAccount();


        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(todoRepository.findById(todoId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> todoService.deleteTodo(userId, todoId)
        );

        assertEquals(ErrorCode.TODO_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void todo의_삭제시_유저가_다른_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long todoId = 1L;

        Todo todo = TodoFixture.get("testUserId");
        UserAccount writer = UserAccountFixture.get(userId, "password");


        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(writer));
        when(todoRepository.findById(todoId))
                .thenReturn(Optional.of(todo));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> todoService.deleteTodo(userId, todoId)
        );

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void todo_리스트의_조회가_성공한_경우() {
        // Given
        String userId = "userId";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));

        // Then
        assertDoesNotThrow(() -> todoService.getMyTodos(userId));
    }
}