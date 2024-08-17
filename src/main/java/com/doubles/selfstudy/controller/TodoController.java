package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.response.ImportanceTypeResponse;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.dto.todo.ImportanceType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/main/todo")
@RestController
public class TodoController {

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
}
