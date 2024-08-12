package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.QuestionBoardRequest;
import com.doubles.selfstudy.controller.response.QuestionBoardResponse;
import com.doubles.selfstudy.controller.response.QuestionBoardTagResponse;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.dto.question.QuestionBoardDto;
import com.doubles.selfstudy.dto.question.QuestionBoardTag;
import com.doubles.selfstudy.service.QuestionBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/main/question_board")
@RestController
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;
    
    // 게시글 리스트 조회
    @GetMapping
    public Response<Page<QuestionBoardResponse>> questionBoardList(
                Authentication authentication,
                @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // question board list 반환
        return Response.success(
                questionBoardService
                        .questionBoardList(pageable)
                        .map(QuestionBoardResponse::fromQuestionBoardDto)
                );
    }

    // 태그를 통한 게시글 리스트 조회
    @GetMapping("/tag/{tag}")
    public Response<Page<QuestionBoardResponse>> questionBoardListByTag(
            Authentication authentication,
            @PathVariable String tag,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return Response.success(
                questionBoardService
                        .questionBoardListByTag(tag, pageable)
                        .map(QuestionBoardResponse::fromQuestionBoardDto)
        );
    }
    
    // 내 게시글 조회
    @GetMapping("/my")
    public Response<Page<QuestionBoardResponse>> myQuestionBoardList(
                 Authentication authentication,
                 @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // my question board list 반환
        return Response.success(
                questionBoardService
                        .myQuestionBoardList(authentication.getName(), pageable)
                        .map(QuestionBoardResponse::fromQuestionBoardDto)
                );
    }
    
    // 게시글 상세 조회
    @GetMapping("/{questionBoardId}")
    public Response<QuestionBoardResponse> questionBoardDetail(@PathVariable Long questionBoardId) {
        // question board detail data 반환

        return Response.success(
                QuestionBoardResponse.fromQuestionBoardDto(
                        questionBoardService.questionBoardDetail(questionBoardId)
                )
        );
    }
    
    // 게시글 생성
    @PostMapping
    public Response<Void> createQuestionBoard(
                Authentication authentication,
                @RequestBody QuestionBoardRequest request
    ) {
        // create page 생성
        questionBoardService.createQuestionBoard(
                authentication.getName(), request.getTitle(), request.getContent(), request.getTag());

        return Response.success();
    }

    // 게시글 수정
    @PutMapping("/{questionBoardId}")
    public Response<QuestionBoardResponse> modifyQuestionBoard(
                Authentication authentication,
                @PathVariable Long questionBoardId,
                @RequestBody QuestionBoardRequest request
    ) {
        QuestionBoardDto questionBoard = questionBoardService.modifyQuestionBoard(
                authentication.getName(),
                questionBoardId,
                request.getTitle(),
                request.getContent(),
                request.getTag()
        );

        return Response.success(QuestionBoardResponse.fromQuestionBoardDto(questionBoard));
    }
    
    // 게시글 삭제
    @DeleteMapping("/{questionBoardId}")
    public Response<Void> deleteQuestionBoard(
            Authentication authentication,
            @PathVariable Long questionBoardId
    ) {
        questionBoardService.deleteQuestionBoard(authentication.getName(), questionBoardId);

        return Response.success();
    }
    
    // 좋아요 기능
    @PostMapping("/{questionBoardId}/like")
    public Response<Void> questionBoardLike(
            Authentication authentication,
            @PathVariable Long questionBoardId
    ) {
        questionBoardService.questionBoardLike(authentication.getName(), questionBoardId);

        return Response.success();
    }

    // 좋아요 삭제 기능
    @DeleteMapping("/{questionBoardId}/like")
    public Response<Void> questionBoardDisLike(
            Authentication authentication,
            @PathVariable Long questionBoardId
    ) {
        questionBoardService.questionBoardDisLike(authentication.getName(), questionBoardId);

        return Response.success();
    }
    
    // 좋아요 갯수 조회
    @GetMapping("/{questionBoardId}/like")
    public Response<Long> questionBoardLikeCount(
            @PathVariable Long questionBoardId,
            Authentication authentication
    ) {
        return Response.success(questionBoardService.questionBoardLikeCount(questionBoardId));
    }

    @GetMapping("/tags")
    public Response<List<QuestionBoardTagResponse>> getTags() {
        return Response.success(Arrays.stream(QuestionBoardTag.values())
                .map(tag ->
                        QuestionBoardTagResponse.of(
                                tag.name(), tag.getTagName()))
                .collect(Collectors.toList())
        );
    }
}
