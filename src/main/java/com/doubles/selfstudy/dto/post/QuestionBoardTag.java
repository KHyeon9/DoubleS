package com.doubles.selfstudy.dto.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionBoardTag {
    Free("자유"),
    Certificate("자격증"),
    IT("IT"),
    English("영어"),
    Employment("취업"),
    Etc("기타");

    @Getter
    private final String tagName;
}
