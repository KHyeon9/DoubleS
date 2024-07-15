package com.doubles.selfstudy.exception;

import com.doubles.selfstudy.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DoubleSApplicationException.class)
    public ResponseEntity<?> applicationHandler(DoubleSApplicationException e) {
        log.error("Error 발생 {}", e.toString());

        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }

    // RuntimeException 발생시 에러 출력 및 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException e) {
        log.error("Error 발생 {}", e.toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(ErrorCode.INVALID_PASSWORD.name()));
    }
}
