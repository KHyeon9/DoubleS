package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.QuestionBoardCreaeteRequest;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.service.QuestionBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/main/post")
@RestController
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;

    @GetMapping
    public void questionBoardList() {
        // question board list 반환
    }

    @GetMapping("/{questionBoardId}")
    public void questionBoardDetail(@PathVariable Integer questionBoardId) {
        // question board detail data 반환
    }

    @PostMapping("/create")
    public Response<Void> createQuestionBoard(@RequestBody QuestionBoardCreaeteRequest request, Authentication authentication) {
        // create page 생성
        questionBoardService.createPost(request.getTitle(), request.getContent(), authentication.getName());

        return Response.success();
    }
}
