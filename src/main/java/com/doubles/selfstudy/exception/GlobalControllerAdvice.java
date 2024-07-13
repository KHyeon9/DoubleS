package com.doubles.selfstudy.exception;

import com.doubles.selfstudy.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DoubleSApplicationException.class)
    public ResponseEntity<?> applicationHandler(DoubleSApplicationException e) {
        log.error("Error occurs {}", e.toString());

        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }
}
