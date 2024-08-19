package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.todo.ImportanceType;
import com.doubles.selfstudy.dto.todo.TodoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TodoResponse {

    private Long id;
    private UserResponse user;
    private String content;
    private ImportanceTypeResponse importanceType;
    private boolean isCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static TodoResponse fromTodoDto(TodoDto dto) {
        return new TodoResponse(
                dto.getId(),
                UserResponse.fromUserAccountDto(dto.getUserAccountDto()),
                dto.getContent(),
                new ImportanceTypeResponse(
                        dto.getImportanceType().name(),
                        dto.getImportanceType().getTypeName()
                ),
                dto.isCompleted(),
                dto.getCreatedAt(),
                dto.getModifiedAt()
        );
    }
}
