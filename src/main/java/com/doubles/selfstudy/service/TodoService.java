package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.todo.ImportanceType;
import com.doubles.selfstudy.dto.todo.TodoDto;
import com.doubles.selfstudy.entity.Todo;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.TodoRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final ServiceUtils serviceUtils;

    // 나의 Todo 리스트 가져오기
    public List<TodoDto> getMyTodos(String userId) {
        // 유저 가저오기
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        return todoRepository.findAllByUserAccount(userAccount)
                .stream()
                .map(TodoDto::fromEntity)
                .toList();
    }

    // 나의 완료된 Todo 갯수 가져오기
    public Integer getMyCompletedTodoCount(String userId) {
        return todoRepository.countCompletedByUserId(userId);
    }

    // Todo 생성
    @Transactional
    public void createTodo(String userId, String content, String importanceType) {
        // 유저 정보 가져오기
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);
        
        // 스트링을 타입으로 변경
        ImportanceType importanceTypeValue = ImportanceType.fromString(importanceType);

        todoRepository.save(Todo.of(userAccount, content, importanceTypeValue));
    }
    
    // 완료 또는 완료 못함 변경
    @Transactional
    public TodoDto changeTodoCompletedStatus(String userId, Long todoId) {
        // 유저 정보 가져오기
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // todo 가져오기
        Todo todo = serviceUtils.getTodoOrException(todoId);

        // 유저가 다른 경우 검사
        if (!Objects.equals(todo.getUserAccount().getUserId(), userAccount.getUserId())) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                        "%s는 권한이 todo 번호: '%s' 에 대해서 권한이 없습니다.",
                        userId,
                        todoId
                    )
                );
        }

        todo.setCompleted(!todo.isCompleted());

        return TodoDto.fromEntity(todoRepository.saveAndFlush(todo));
    }
    
    // 내용 변경
    @Transactional
    public TodoDto modifyTodoInfo(String userId, Long todoId, String content, String importanceType) {
        // 유저 정보 가져오기
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // todo 가져오기
        Todo todo = serviceUtils.getTodoOrException(todoId);

        // 유저가 다른 경우 검사
        if (!Objects.equals(todo.getUserAccount().getUserId(), userAccount.getUserId())) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                        "%s는 권한이 todo 번호: '%s' 에 대해서 권한이 없습니다.",
                        userId,
                        todoId
                    )
                );
        }

        ImportanceType importanceTypeValue = ImportanceType.fromString(importanceType);
        todo.setContent(content);
        todo.setImportanceType(importanceTypeValue);

        return TodoDto.fromEntity(todoRepository.saveAndFlush(todo));
    }

    // Todo 삭제
    @Transactional
    public void deleteTodo(String userId, Long todoId) {
        // 유저 정보 가져오기
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // todo 가져오기
        Todo todo = serviceUtils.getTodoOrException(todoId);

        // 유저가 다른 경우 검사
        if (!Objects.equals(todo.getUserAccount().getUserId(), userAccount.getUserId())) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                        "%s는 권한이 todo 번호: '%s' 에 대해서 권한이 없습니다.",
                        userId,
                        todoId
                    )
                );
        }

        todoRepository.deleteById(todoId);
    }
}
