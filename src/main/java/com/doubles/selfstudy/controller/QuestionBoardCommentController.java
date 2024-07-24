package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.QuestionBoardCommentRequest;
import com.doubles.selfstudy.controller.response.QuestionBoardCommentResponse;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.service.QuestionBoardCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/main/question_board")
@RestController
public class QuestionBoardCommentController {

    private final QuestionBoardCommentService questionBoardCommentService;

    // 댓글 리스트
    @GetMapping("/{questionBoardId}/comment")
    public Response<Page<QuestionBoardCommentResponse>> questionBoardCommentList(
            @PathVariable Long questionBoardId,
            Pageable pageable,
            Authentication authentication
    ) {

        return Response.success(
                questionBoardCommentService.questionBoardCommentList(questionBoardId, pageable)
                        .map(QuestionBoardCommentResponse::fromQuestionBoardCommentDto)
        );
    }

    // 댓글 쓰기
    @PostMapping("/{questionBoardId}/comment")
    public Response<Void> createQuestionBoardComment(
            @PathVariable Long questionBoardId,
            @RequestBody QuestionBoardCommentRequest request,
            Authentication authentication
    ) {
        questionBoardCommentService.createQuestionBoardComment(
                authentication.getName(), questionBoardId, request.getComment());

        return Response.success();
    }

    // 댓글 수정
    @PutMapping("/{questionBoardId}/comment")
    public Response<Void> updateQuestionBoardComment(
            @PathVariable Long questionBoardId,
            @RequestBody QuestionBoardCommentRequest request,
            Authentication authentication
    ) {


        return Response.success();
    }

    // 댓글 삭제
    @DeleteMapping("/{questionBoardId}/comment")
    public Response<Void> deleteQuestionBoardComment(

    ) {


        return Response.success();
    }
}
