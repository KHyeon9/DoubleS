package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.TodoRequest;
import com.doubles.selfstudy.dto.todo.TodoDto;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.TodoFixture;
import com.doubles.selfstudy.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    @Test
    @WithMockUser
    void todo_작성_성공() throws Exception {
        // Given
        String content = "content";
        String importanceType = "Middle";

        // When & Then
        mockMvc.perform(post("/api/main/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new TodoRequest(content, importanceType)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void todo_작성시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given
        String content = "content";
        String importanceType = "Middle";

        // When & Then
        mockMvc.perform(post("/api/main/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new TodoRequest(content, importanceType)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void todo_정보_수정_성공한_경우() throws Exception {
        // Given
        String content = "modify_content";
        String importanceType = "High";

        // When
        when(todoService.modifyTodoInfo(any(), any(), eq(content), eq(importanceType)))
                .thenReturn(TodoDto.fromEntity(TodoFixture.get("userId")));

        // Then
        mockMvc.perform(put("/api/main/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new TodoRequest(content, importanceType)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void todo_정보_수정시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given
        String content = "modify_content";
        String importanceType = "High";

        // When
        when(todoService.modifyTodoInfo(any(), any(), eq(content), eq(importanceType)))
                .thenReturn(TodoDto.fromEntity(TodoFixture.get("userId")));

        // Then
        mockMvc.perform(put("/api/main/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new TodoRequest(content, importanceType)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void todo_정보_수정시_작성자가_다른_경우_에러_발생() throws Exception {
        // Given
        String content = "modify_content";
        String importanceType = "High";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(todoService).modifyTodoInfo(any(), any(), eq(content), eq(importanceType));

        // Then
        mockMvc.perform(put("/api/main/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new TodoRequest(content, importanceType)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void todo_정보_수정시_해당하는_todo가_없는_경우_에러_발생() throws Exception {
        // Given
        String content = "modify_content";
        String importanceType = "High";

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.TODO_NOT_FOUND))
                .when(todoService).modifyTodoInfo(any(), any(), eq(content), eq(importanceType));

        // Then
        mockMvc.perform(put("/api/main/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new TodoRequest(content, importanceType)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.TODO_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void todo_완료_상태_수정_성공한_경우() throws Exception {
        // Given

        // When
        when(todoService.changeTodoCompletedStatus(any(), any()))
                .thenReturn(TodoDto.fromEntity(TodoFixture.get("userId")));

        // Then
        mockMvc.perform(put("/api/main/todo/1/change_status")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void todo_완료_상태_수정시_로그인이_안된_경우_에러_발생() throws Exception {
        // Given

        // When
        when(todoService.changeTodoCompletedStatus(any(), any()))
                .thenReturn(TodoDto.fromEntity(TodoFixture.get("userId")));

        // Then
        mockMvc.perform(put("/api/main/todo/1/change_status")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void todo_완료_상태_수정시_작성자와_유저가_다른_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(todoService).changeTodoCompletedStatus(any(), any());

        // Then
        mockMvc.perform(put("/api/main/todo/1/change_status")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void todo_완료_상태_수정시_해당하는_todo가_없는_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.TODO_NOT_FOUND))
                .when(todoService).changeTodoCompletedStatus(any(), any());

        // Then
        mockMvc.perform(put("/api/main/todo/1/change_status")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.TODO_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void todo_삭제가_성공한_경우() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void todo_삭제시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given

        // When

        // Then
        mockMvc.perform(delete("/api/main/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void todo_삭제시_작성자와_유저가_다른_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION))
                .when(todoService).deleteTodo(any(), any());

        // Then
        mockMvc.perform(delete("/api/main/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void todo_삭제시_해당하는_todo가_없는_경우_에러_발생() throws Exception {
        // Given

        // When
        doThrow(new DoubleSApplicationException(ErrorCode.TODO_NOT_FOUND))
                .when(todoService).deleteTodo(any(), any());

        // Then
        mockMvc.perform(delete("/api/main/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.TODO_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 나의_todo리스트_조회_성공하는_경우() throws Exception {
        // Given
        List<TodoDto> todoList = todoService.getMyTodos(any());

        // When
        when(todoList).thenReturn(List.of());

        // Then
        mockMvc.perform(get("/api/main/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 나의_todo리스트_조회시_로그인_안한_경우_에러_발생() throws Exception {
        // Given
        List<TodoDto> todoList = todoService.getMyTodos(any());

        // When
        when(todoList).thenReturn(List.of());

        // Then
        mockMvc.perform(get("/api/main/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }
}