package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.response.StudyGroupUserResponse;
import com.doubles.selfstudy.dto.studygroup.UserStudyGroupDto;
import com.doubles.selfstudy.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/main/study_group")
@RestController
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    // 스터디 그룹원 리스트 조회
    public List<StudyGroupUserResponse> studyGroupUserList(
            Authentication authentication
    ) {
        List<UserStudyGroupDto> userStudyGroupDtos =
                studyGroupService.studyGroupMemberList(authentication.getName());

        return userStudyGroupDtos
                .stream()
                .map(StudyGroupUserResponse::fromUserStudyGroupDto)
                .toList();
    }
}
