package com.doubles.selfstudy.dto.question;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionBoardTag {
    Free("자유"),
    Certificate("자격증"),
    IT("IT"),
    English("영어"),
    Employment("취업");

    private final String tagName;

    // String 값을 받아 Enum 값으로 변환하는 메서드
    public static QuestionBoardTag fromString(String tagName) {
        for (QuestionBoardTag questionBoardTag : QuestionBoardTag.values()) {
            if (questionBoardTag.name().equalsIgnoreCase(tagName)) {
                return questionBoardTag;
            }
        }
        throw new IllegalArgumentException("Tag를 찾지 못했습니다 tag: " + tagName);
    }
}
