package com.doubles.selfstudy.fixture;

import com.doubles.selfstudy.dto.todo.ImportanceType;
import com.doubles.selfstudy.entity.Todo;
import com.doubles.selfstudy.entity.UserAccount;

public class TodoFixture {

    public static Todo get(String userId) {
        UserAccount userAccount = UserAccount.of(userId, "test", "test", "test", null);

        Todo todo = Todo.of(userAccount, "content", ImportanceType.Middle, false);

        return todo;
    }

    public static Todo get(String userId, boolean isCompleted) {
        UserAccount userAccount = UserAccount.of(userId, "test", "test", "test", null);

        Todo todo = Todo.of(userAccount, "content", ImportanceType.Middle, isCompleted);

        return todo;
    }

    public static Todo get(String userId, String content, String importanceType) {
        UserAccount userAccount = UserAccount.of(userId, "test", "test", "test", null);

        ImportanceType importanceTypeValue = ImportanceType.fromString(importanceType);

        Todo todo = Todo.of(userAccount, content, importanceTypeValue, false);

        return todo;
    }
}
