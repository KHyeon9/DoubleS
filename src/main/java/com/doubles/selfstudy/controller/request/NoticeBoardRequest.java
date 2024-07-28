package com.doubles.selfstudy.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeBoardRequest {

    private String title;
    private String content;
}
