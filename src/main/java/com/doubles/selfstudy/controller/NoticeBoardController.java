package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.NoticeBoardRequest;
import com.doubles.selfstudy.controller.response.NoticeBoardResponse;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.dto.notice.NoticeBoardDto;
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
        return Response.success(
                noticeBoardService.noticeBoardList(pageable)
                        .map(NoticeBoardResponse::fromNoticeBoardDto)
        );
    }

    // 공지사항 상세 조회
    @GetMapping("/{noticeBoardId}")
    public Response<NoticeBoardResponse> noticeBoardDetail(@PathVariable Long noticeBoardId) {
        return Response.success(
                NoticeBoardResponse.fromNoticeBoardDto(
                        noticeBoardService.noticeBoardDetail(noticeBoardId)
                )
        );
    }

    // 공지사항 생성
    @PostMapping
    public Response<Void> createNoticeBoard(
            Authentication authentication,
            @RequestBody NoticeBoardRequest request
    ) {
        noticeBoardService.createNoticeBoard(
                authentication.getName(),
                request.getTitle(),
                request.getContent()
        );

        return Response.success();
    }
    
    // 공지사항 수정
    @PutMapping("/{noticeBoardId}")
    public Response<NoticeBoardResponse> modifyNoticeBoard(
            Authentication authentication,
            @PathVariable Long noticeBoardId,
            @RequestBody NoticeBoardRequest request
    ) {
        NoticeBoardDto noticeBoardDto = noticeBoardService.modifyNoticeBoard(
                authentication.getName(),
                noticeBoardId,
                request.getTitle(),
                request.getContent()
        );

        return Response.success(NoticeBoardResponse.fromNoticeBoardDto(noticeBoardDto));
    }

    // 공지사항 삭제
    @DeleteMapping("/{noticeBoardId}")
    public Response<Void> deleteNoticeBoard(
            Authentication authentication,
            @PathVariable Long noticeBoardId
    ) {
        noticeBoardService.deleteNoticeBoard(authentication.getName(), noticeBoardId);

        return Response.success();
    }

}
