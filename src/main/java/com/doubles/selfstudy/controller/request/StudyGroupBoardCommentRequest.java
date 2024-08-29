package com.doubles.selfstudy.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
// deserialize 오류로 인한 빈 생성자 추가, 아마 필드가 하나만 있는 경우 문제가 생기는 듯
@NoArgsConstructor
public class StudyGroupBoardCommentRequest {

    private String comment;
}
