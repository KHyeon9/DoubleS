package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.response.NoticeBoardResponse;
import com.doubles.selfstudy.controller.response.QuestionBoardResponse;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.service.HomePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/main")
@RestController
public class HomeController {

    private final HomePageService homePageService;

    @GetMapping("/question_board_list")
    public Response<List<QuestionBoardResponse>> homePageQuestionBoardList() {
        // 최신 공자사항 데이터 및 최신 질문 게시글 데이터 가져오기

        
        return Response.success(
                homePageService
                        .homePageQuestionBoardList()
                        .stream()
                        .map(QuestionBoardResponse::fromQuestionBoardDto)
                        .toList()
        );
    }

    @GetMapping("/notice_board_list")
    public Response<List<NoticeBoardResponse>> homePageNoticeBoardList() {
        return Response.success(
                homePageService
                        .homePageNoticeBoardList()
                        .stream()
                        .map(NoticeBoardResponse::fromNoticeBoardDto)
                        .toList()
        );
    }
}
