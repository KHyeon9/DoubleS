package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.question.QuestionBoardCommentDto;
import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardComment;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.QuestionBoardCommentRepository;
import com.doubles.selfstudy.repository.QuestionBoardRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class QuestionBoardCommentService {

    private final UserAccountRepository userAccountRepository;
    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final QuestionBoardRepository questionBoardRepository;

    // 질문 게시글 댓글 리스트 조회
    public Page<QuestionBoardCommentDto> questionBoardCommentList(Long questionBoardId, Pageable pageable) {
        QuestionBoard questionBoard = getQuestionBoardOrException(questionBoardId);

        return questionBoardCommentRepository
                .findAllByQuestionBoard(questionBoard, pageable)
                .map(QuestionBoardCommentDto::fromEntity);
    }

    // 질문 게시글 댓글 기능
    @Transactional
    public void createQuestionBoardComment(String userId, Long questionBoardId, String comment) {
        // user find
        UserAccount userAccount = getUserAccountOrException(userId);

        // question board find
        QuestionBoard questionBoard = getQuestionBoardOrException(questionBoardId);

        // comment create
        questionBoardCommentRepository.save(QuestionBoardComment.of(comment, userAccount, questionBoard));

    }

    private UserAccount getUserAccountOrException(String userId) {
        // find user account
        return userAccountRepository.findById(userId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("유저 %s를 찾지 못했습니다.", userId))
        );
    }

    private QuestionBoard getQuestionBoardOrException(Long questionBoardId) {
        // find question board
        return questionBoardRepository.findById(questionBoardId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND, String.format("게시글 번호: '%s' 를 찾지 못했습니다.", questionBoardId))
        );
    }
}
