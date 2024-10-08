package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.StudyGroupRequest;
import com.doubles.selfstudy.controller.response.Response;
import com.doubles.selfstudy.controller.response.StudyGroupResponse;
import com.doubles.selfstudy.controller.response.StudyGroupUserResponse;
import com.doubles.selfstudy.controller.response.UserStudyGroupResponse;
import com.doubles.selfstudy.dto.studygroup.StudyGroupDto;
import com.doubles.selfstudy.dto.studygroup.UserStudyGroupDto;
import com.doubles.selfstudy.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/main/study_group")
@RestController
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    // 스터디 그룹 데이터 조회
    @GetMapping
    public Response<StudyGroupResponse> studyGroupInfo(
            Authentication authentication
    ) {
        return Response.success(
                StudyGroupResponse.fromStudyGroupDto(
                    studyGroupService.studyGroupInfo(authentication.getName())
            )
        );
    }

    // 스터디 그룹원 리스트 조회
    @GetMapping("/member")
    public Response<List<StudyGroupUserResponse>> studyGroupUserList(
            Authentication authentication
    ) {
        List<UserStudyGroupDto> userStudyGroupDtos =
                studyGroupService.studyGroupMemberList(authentication.getName());

        return Response.success(
                userStudyGroupDtos
                    .stream()
                    .map(StudyGroupUserResponse::fromUserStudyGroupDto)
                    .toList()
        );
    }

    // 스터디 그룹 생성
    @PostMapping
    public Response<Void> createStudyGroup(
            Authentication authentication,
            @RequestBody StudyGroupRequest request
    ) {
        studyGroupService.createStudyGroup(
                authentication.getName(),
                request.getStudyGroupName(),
                request.getDescription()
        );

        return Response.success();
    }

    // 스터디 그룹 수정
    @PutMapping
    public Response<StudyGroupResponse> modifyStudyGroup(
            Authentication authentication,
            @RequestBody StudyGroupRequest request
    ) {
        StudyGroupDto studyGroupDto = studyGroupService.modifyStudyGroup(
                                                authentication.getName(),
                                                request.getStudyGroupName(),
                                                request.getDescription()
                                        );

        return Response.success(
                StudyGroupResponse.fromStudyGroupDto(studyGroupDto)
        );
    }

    // 스터디 그룹 삭제
    @DeleteMapping
    public Response<Void> deleteStudyGroup(
            Authentication authentication
    ) {
        studyGroupService.deleteStudyGroup(authentication.getName());

        return Response.success();
    }

    // 그룹 초대 알림 전송
    @PostMapping("/invite")
    private Response<Void> inviteStudyGroupMember(
            Authentication authentication,
            @RequestParam String inviteUserId
    ) {
        studyGroupService.inviteAlarmStudyGroupMember(authentication.getName(), inviteUserId);

        return Response.success();
    }

    // 그룹 초대 확인
    @PostMapping("/join")
    private Response<Void> joinStudyGroup(
            Authentication authentication,
            @RequestParam String leaderUserId
    ) {
        studyGroupService.joinStudyGroupMember(authentication.getName(), leaderUserId);

        return Response.success();
    }

    // 스터디 그룹 리더 변경
    @PutMapping("/change_leader")
    private Response<UserStudyGroupResponse> changeStudyGroupLeader(
            Authentication authentication,
            @RequestParam String changeLeaderId
    ) {
        return Response.success(
                UserStudyGroupResponse.fromUserStudyGroupDto(
                        studyGroupService.changeStudyGroupLeader(authentication.getName(), changeLeaderId)
                )
        );
    }

    // 그룹 강퇴
    @DeleteMapping("/force_exit")
    private Response<Void> forceExitStudyGroupMember(
            Authentication authentication,
            @RequestParam String deleteUserId
    ) {
        studyGroupService.forceExitStudyGroupMember(authentication.getName(), deleteUserId);

        return Response.success();
    }

    // 그룹 탈퇴
    @DeleteMapping("/exit")
    private Response<Void> exitStudyGroup(
            Authentication authentication
    ) {
        studyGroupService.exitStudyGroupMySelf(authentication.getName());

        return Response.success();
    }
}
