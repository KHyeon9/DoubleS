package com.doubles.selfstudy.controller.response;

import com.doubles.selfstudy.dto.studygroup.StudyGroupBoardDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyGroupBoardResponse {

    private Long id;
    private UserResponse user;
    private String title;
    private String content;
    private int comments;

    public static StudyGroupBoardResponse fromStudyGroupBoardDto(StudyGroupBoardDto dto) {
        return new StudyGroupBoardResponse(
                dto.getId(),
                UserResponse.fromUserAccountDto(dto.getUser()),
                dto.getTitle(),
                dto.getContent(),
                dto.getComments()
        );
    }
}
