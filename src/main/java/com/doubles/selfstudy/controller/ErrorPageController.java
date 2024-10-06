package com.doubles.selfstudy.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController implements ErrorController {

    //    @GetMapping("/error")
    //    public String redirectRoot() {
    //        return "index.html";
    //    }
    @RequestMapping("/error") // Spring Boot의 기본 에러 핸들러
    public String handleError() {
        // 에러가 발생하면 index.html로 포워딩
        return "forward:/index.html";
    }
}
