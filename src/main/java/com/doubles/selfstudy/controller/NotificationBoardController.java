package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.NotificationBoardRequest;
import com.doubles.selfstudy.controller.response.NotificationBoardResponse;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.service.NotificationBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/main/notification_board")
@RestController
public class NotificationBoardController {

    private final NotificationBoardService notificationBoardService;

    // 공지사항 리스트 조회
    @GetMapping
    public Response<Page<NotificationBoardResponse>> notificationBoardList(
            Authentication authentication,
            Pageable pageable
    ) {
        // 공지사항 리스트 반환
        return Response.success(null);
    }

    // 공지사항 생성
    @PostMapping
    public Response<Void> createNotificationBoard(
            Authentication authentication,
            @RequestBody NotificationBoardRequest request
    ) {

        return Response.success();
    }
    
    // 공지사항 수정
    @PutMapping
    public Response<Void> updateNotificationBoard(
            Authentication authentication,
            @RequestBody NotificationBoardRequest request
    ) {
        return Response.success();
    }

    // 공지사항 삭제
    @DeleteMapping
    public Response<Void> deleteNotificationBoard(
            Authentication authentication,
            Long notificationBoardId
    ) {
        return Response.success();
    }

}
