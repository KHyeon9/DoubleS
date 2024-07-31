package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/main")
@RestController
public class HomeController {
    
    @GetMapping
    public Response<Void> mainList() {
        // 최신 공자사항 데이터 및 최신 질문 게시글 데이터 가져오기
        
        return Response.success();
    }
}
