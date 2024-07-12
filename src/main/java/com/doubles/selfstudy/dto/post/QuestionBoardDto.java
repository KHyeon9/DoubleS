package com.doubles.selfstudy.dto.post;

import com.doubles.selfstudy.entity.UserAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionBoardDto {

    private Long id;
    private UserAccount userAccount; // 유저 정보
    private String title; // 제목
    private String content; // 내용
    private QuestionBoardTag tag; // 태그


}
