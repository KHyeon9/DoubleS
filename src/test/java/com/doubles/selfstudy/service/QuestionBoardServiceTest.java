package com.doubles.selfstudy.service;

import com.doubles.selfstudy.repository.QuestionBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class QuestionBoardServiceTest {

    @Autowired
    private QuestionBoardService postService;

    @MockBean
    private QuestionBoardRepository questionBoardRepository;


}