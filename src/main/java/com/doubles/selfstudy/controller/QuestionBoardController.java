package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.QuestionBoardCreateRequest;
import com.doubles.selfstudy.controller.request.QuestionBoardModifyRequest;
import com.doubles.selfstudy.controller.response.QuestionBoardResponse;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.dto.post.QuestionBoardDto;
import com.doubles.selfstudy.service.QuestionBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/main/question_board")
@RestController
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;

    @GetMapping
    public Response<Page<QuestionBoardResponse>> questionBoardList(
                Pageable pageable,
                Authentication authentication
    ) {
        // question board list 반환
        return Response.success(
                questionBoardService
                        .questionBoardList(pageable)
                        .map(QuestionBoardResponse::fromQuetionBoardDto)
                );
    }

    @GetMapping("/my")
    public Response<Page<QuestionBoardResponse>> myQuestionBoardList(
                 Pageable pageable,
                 Authentication authentication
    ) {
        // my question board list 반환
        return Response.success(
                questionBoardService
                        .myQuestionBoardList(authentication.getName(), pageable)
                        .map(QuestionBoardResponse::fromQuetionBoardDto)
                );
    }

    @GetMapping("/{questionBoardId}")
    public void questionBoardDetail(@PathVariable Integer questionBoardId) {
        // question board detail data 반환
    }

    @PostMapping
    public Response<Void> createQuestionBoard(
                @RequestBody QuestionBoardCreateRequest request,
                Authentication authentication
    ) {
        // create page 생성
        questionBoardService.createQuestionBoard(
                request.getTitle(), request.getContent(), authentication.getName());

        return Response.success();
    }

    @PutMapping("/{questionBoardId}")
    public Response<QuestionBoardResponse> modifyQuestionBoard(
                @PathVariable Long questionBoardId,
                @RequestBody QuestionBoardModifyRequest request,
                Authentication authentication
    ) {
        QuestionBoardDto questionBoard = questionBoardService.modifyQuestionBoard(
                request.getTitle(),
                request.getContent(),
                authentication.getName(),
                questionBoardId
        );

        return Response.success(QuestionBoardResponse.fromQuetionBoardDto(questionBoard));
    }

    @DeleteMapping("/{questionBoardId}")
    public Response<Void> deleteQuestionBoard(
            @PathVariable Long questionBoardId,
            Authentication authentication
    ) {
        questionBoardService.deleteQuestionBoard(authentication.getName(), questionBoardId);

        return Response.success();
    }

}
