package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.StudyGroupBoardCommentRequest;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.controller.response.StudyGroupBoardCommentResponse;
import com.doubles.selfstudy.service.StudyGroupBoardCommentService;
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
public class StudyGroupBoardCommentController {

    private final StudyGroupBoardCommentService studyGroupBoardCommentService;

    // 스터디 그룹 게시글의 댓글 리스트
    @GetMapping("/{studyGroupBoardId}/comment")
    public Response<Page<StudyGroupBoardCommentResponse>> studyGroupBoardCommentList(
            @PathVariable Long studyGroupBoardId,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return Response.success(
                studyGroupBoardCommentService
                        .studyGroupBoardCommentList(
                                studyGroupBoardId,
                                pageable
                        )
                        .map(StudyGroupBoardCommentResponse::fromStudyGroupBoardCommentDto)
        );
    }

    // 스터디 그룹 게시글의 댓글 작성
    @PostMapping("/{studyGroupBoardId}/comment")
    public Response<Void> createStudyGroupBoardComment(
            Authentication authentication,
            @PathVariable Long studyGroupBoardId,
            @RequestBody StudyGroupBoardCommentRequest request
            ) {
        studyGroupBoardCommentService.createStudyGroupBoardCommnet(
                authentication.getName(),
                studyGroupBoardId,
                request.getComment()
        );

        return Response.success();
    }

    // 스터디 그룹 게시글의 댓글 수정
    @PutMapping("/{studyGroupBoardId}/comment/{studyGroupBoardCommentId}")
    public Response<StudyGroupBoardCommentResponse> modifyStudyGroupBoardComment(
            Authentication authentication,
            @PathVariable Long studyGroupBoardCommentId,
            @RequestBody StudyGroupBoardCommentRequest request
    ) {
        return Response.success(
                StudyGroupBoardCommentResponse
                        .fromStudyGroupBoardCommentDto(
                            studyGroupBoardCommentService
                                    .modifyStudyGroupBoardComment(
                                            authentication.getName(),
                                            studyGroupBoardCommentId,
                                            request.getComment()
                                    )
                        )
        );
    }

    // 스터디 그룹 게시글의 댓글 삭제
    @DeleteMapping("/{studyGroupBoardId}/comment/{studyGroupBoardCommentId}")
    public Response<Void> deleteStudyGroupBoardComment(
            Authentication authentication,
            @PathVariable Long studyGroupBoardCommentId
    ) {
        studyGroupBoardCommentService.deleteStudyGroupBoardComment(
                authentication.getName(),
                studyGroupBoardCommentId
        );

        return Response.success();
    }
}
