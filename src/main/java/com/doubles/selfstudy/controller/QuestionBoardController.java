package com.doubles.selfstudy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/post")
@RestController
public class QuestionBoardController {

    @GetMapping
    public void questionBoardList() {
        // question board list 반환
    }
}
