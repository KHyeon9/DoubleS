package com.doubles.selfstudy.dto.todo;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TodoDto {

    private Long id;
    private UserAccountDto userAccountDto;
    private String content;
    private ImportanceType importanceType;
    private boolean isCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static TodoDto of(UserAccountDto userAccountDto, String content, ImportanceType importanceType, boolean isCompleted) {
        return TodoDto.of(null, userAccountDto, content, importanceType, isCompleted, null, null);
    }

    public static TodoDto of(Long id, UserAccountDto userAccountDto, String content, ImportanceType importanceType, boolean isCompleted, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new TodoDto(id, userAccountDto, content, importanceType, isCompleted, createdAt, modifiedAt);
    }

    public static TodoDto fromEntity(Todo entity) {
        return new TodoDto(
                entity.getId(),
                UserAccountDto.fromEntity(entity.getUserAccount()),
                entity.getContent(),
                entity.getImportanceType(),
                entity.isCompleted(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
