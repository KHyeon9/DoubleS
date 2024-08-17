package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.notice.NoticeBoardDto;
import com.doubles.selfstudy.dto.question.QuestionBoardDto;
import com.doubles.selfstudy.repository.NoticeBoardRepository;
import com.doubles.selfstudy.repository.QuestionBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HomePageService {

    private final QuestionBoardRepository questionBoardRepository;
    private final NoticeBoardRepository noticeBoardRepository;

    public List<QuestionBoardDto> homePageQuestionBoardList() {
        return questionBoardRepository
                        .findTop5ByOrderByCreatedAtDesc()
                        .stream()
                        .map(QuestionBoardDto::fromEntity)
                        .toList();
    }

    public List<NoticeBoardDto> homePageNoticeBoardList() {
        return noticeBoardRepository
                .findTop10ByOrderByIdDesc()
                .stream()
                .map(NoticeBoardDto::fromEntity)
                .toList();
    }
}
