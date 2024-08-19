package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.TodoRequest;
import com.doubles.selfstudy.controller.response.ImportanceTypeResponse;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.controller.response.TodoResponse;
import com.doubles.selfstudy.dto.todo.ImportanceType;
import com.doubles.selfstudy.dto.todo.TodoDto;
import com.doubles.selfstudy.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/main/todo")
@RestController
public class TodoController {

    private final TodoService todoService;

    // todo리스트 반환
    @GetMapping
    public Response<List<TodoResponse>> getTodoList(
            Authentication authentication
    ) {
        return Response.success(
                todoService.getMyTodos(authentication.getName())
                        .stream().map(TodoResponse::fromTodoDto)
                        .toList()
        );
    }

    // todo 생성
    @PostMapping
    public Response<Void> createTodo(
            Authentication authentication,
            @RequestBody TodoRequest request
    ) {
        todoService.createTodo(
                authentication.getName(),
                request.getContent(),
                request.getImportanceType()
        );

        return Response.success();
    }

    // todo 정보 수정
    @PutMapping("/{todoId}")
    public Response<TodoResponse> modifyTodoInfo(
            Authentication authentication,
            @PathVariable Long todoId,
            @RequestBody TodoRequest request
            ) {
        TodoDto todoDto = todoService.modifyTodoInfo(
                authentication.getName(),
                todoId,
                request.getContent(),
                request.getImportanceType()
        );

        return Response.success(
                TodoResponse.fromTodoDto(todoDto)
        );
    }

    // todo 완료 상태 변경
    @PutMapping("/{todoId}/change_status")
    public Response<TodoResponse> changeTodoCompletedStatus(
            Authentication authentication,
            @PathVariable Long todoId
    ) {
        TodoDto todoDto = todoService.changeTodoCompletedStatus(authentication.getName(), todoId);

        return Response.success(
                TodoResponse.fromTodoDto(todoDto)
        );
    }

    // todo 삭제
    @DeleteMapping("/{todoId}")
    public Response<Void> deleteTodo(
            Authentication authentication,
            @PathVariable Long todoId
    ) {
        todoService.deleteTodo(authentication.getName(), todoId);

        return Response.success();
    }


    // 중요도 리스트 반환
    @GetMapping("/importance_types")
    public Response<List<ImportanceTypeResponse>> getImportanceTypes() {
        return Response.success(
                Arrays.stream(ImportanceType.values())
                        .map(type -> ImportanceTypeResponse.of(
                                type.name(), type.getTypeName()
                        )
                    )
                    .collect(Collectors.toList())
        );
    }

    // 완료된 todo 갯수 조회
    @GetMapping("/totalCompletedCount")
    public Response<Integer> getTotalCompleteCount(Authentication authentication) {
        return Response.success(
                todoService.getMyCompletedTodoCount(authentication.getName())
        );
    }
}
