package com.doubles.selfstudy.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionBoardTagResponse {

    private final String key;
    private final String value;

    public static QuestionBoardTagResponse of(String key, String value) {
        return new QuestionBoardTagResponse(key, value);
    }
}
