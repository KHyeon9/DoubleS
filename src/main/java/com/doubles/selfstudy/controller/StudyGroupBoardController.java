package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.StudyGroupBoardRequest;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.controller.response.StudyGroupBoardResponse;
import com.doubles.selfstudy.service.StudyGroupBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/main/study_group/board")
@RestController
public class StudyGroupBoardController {

    private final StudyGroupBoardService studyGroupBoardService;

    // 스터디 그룹 게시글 리스트 조회
    @GetMapping
    public Response<Page<StudyGroupBoardResponse>> studyGroupBoardList(
            Authentication authentication,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return Response.success(
                studyGroupBoardService
                        .studyGroupBoardList(authentication.getName(), pageable)
                        .map(StudyGroupBoardResponse::fromStudyGroupBoardDto)
        );
    }

    // 스터디 그룹 게시글 상세 조회
    @GetMapping("/{studyGroupBoardId}")
    public Response<StudyGroupBoardResponse> studyGroupBoardDetail(
        Authentication authentication,
        @PathVariable Long studyGroupBoardId
    ) {
        return Response.success(
                StudyGroupBoardResponse.fromStudyGroupBoardDto(
                        studyGroupBoardService
                                .studyGroupBoardDetail(authentication.getName(), studyGroupBoardId)
                )
        );
    }

    // 스터디 그룹 게시글 생성
    @PostMapping
    public Response<Void> createStudyGroupBoard(
            Authentication authentication,
            @RequestBody StudyGroupBoardRequest request
    ) {
        studyGroupBoardService.createStudyGroupBoard(
                authentication.getName(),
                request.getTitle(),
                request.getContent()
        );

        return Response.success();
    }

    // 스터디 그룹 게시글 수정
    @PutMapping("/{studyGroupBoardId}")
    public Response<StudyGroupBoardResponse> modifyStudyGroupBoard(
            Authentication authentication,
            @PathVariable Long studyGroupBoardId,
            @RequestBody StudyGroupBoardRequest request
    ) {
        return Response.success(
                StudyGroupBoardResponse.fromStudyGroupBoardDto(
                        studyGroupBoardService
                                .modifyStudyGroupBoard(
                                        authentication.getName(),
                                        studyGroupBoardId,
                                        request.getTitle(),
                                        request.getContent()
                                )
                )
        );
    }

    // 스터디 그룹 게시글 삭제
    @DeleteMapping("/{studyGroupBoardId}")
    public Response<Void> deleteStudyGroupBoard(
            Authentication authentication,
            @PathVariable Long studyGroupBoardId
    ) {
        studyGroupBoardService.deleteStudyGroupBoard(
                authentication.getName(),
                studyGroupBoardId
        );

        return Response.success();
    }
}
