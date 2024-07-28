package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.NoticeBoardRequest;
import com.doubles.selfstudy.controller.response.NoticeBoardResponse;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/main/notice_board")
@RestController
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;

    // 공지사항 리스트 조회
    @GetMapping
    public Response<Page<NoticeBoardResponse>> noticeBoardList(
            Authentication authentication,
            Pageable pageable
    ) {
        // 공지사항 리스트 반환
        return Response.success(null);
    }

    // 공지사항 생성
    @PostMapping
    public Response<Void> createNoticeBoard(
            Authentication authentication,
            @RequestBody NoticeBoardRequest request
    ) {

        return Response.success();
    }
    
    // 공지사항 수정
    @PutMapping
    public Response<Void> updateNoticeBoard(
            Authentication authentication,
            @RequestBody NoticeBoardRequest request,
            Long noticeBoardId
    ) {
        return Response.success();
    }

    // 공지사항 삭제
    @DeleteMapping
    public Response<Void> deleteNoticeBoard(
            Authentication authentication,
            Long noticeBoardId
    ) {
        return Response.success();
    }

}
