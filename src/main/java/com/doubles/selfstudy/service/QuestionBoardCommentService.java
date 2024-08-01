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
        // 질문 게시글 확인
        QuestionBoard questionBoard = getQuestionBoardOrException(questionBoardId);

        return questionBoardCommentRepository
                .findAllByQuestionBoard(questionBoard, pageable)
                .map(QuestionBoardCommentDto::fromEntity);
    }

    // 질문 게시글 댓글 작성 기능
    @Transactional
    public void createQuestionBoardComment(String userId, Long questionBoardId, String comment) {
        // 유저 확인
        UserAccount userAccount = getUserAccountOrException(userId);

        // 질문 게시글 확인
        QuestionBoard questionBoard = getQuestionBoardOrException(questionBoardId);

        // 댓글 저장
        questionBoardCommentRepository.save(QuestionBoardComment.of(comment, userAccount, questionBoard));
    }

    // 질문 게시글 댓글 수정
    public QuestionBoardCommentDto modifyQuestionBoardComment(String userId, Long questionBoardId, Long questionBoardCommentId,  String comment) {
        // user 확인
        UserAccount userAccount = getUserAccountOrException(userId);

        // 게시글 확인
        getQuestionBoardOrException(questionBoardId);

        // 댓글 확인
        QuestionBoardComment questionBoardComment = getQuestionBoardCommentOrException(questionBoardCommentId);

        // 댓글 작성자 확인
        if (questionBoardComment.getUserAccount() != userAccount) {
                throw new DoubleSApplicationException(
                        ErrorCode.INVALID_PERMISSION, String.format(
                        "%s는 권한이 댓글 번호: '%s' 에 대해서 권한이 없습니다.",
                        userId,
                        questionBoardCommentId
                )
            );
        }

        questionBoardComment.setComment(comment);

        return QuestionBoardCommentDto.fromEntity(questionBoardCommentRepository.saveAndFlush(questionBoardComment));
    }

    // 질문 게시글 댓글 삭제
    public void deleteQuestionBoardComment(String userId, Long questionBoardCommentId) {
        // user 확인
        UserAccount userAccount = getUserAccountOrException(userId);

        // 댓글 확인
        QuestionBoardComment questionBoardComment = getQuestionBoardCommentOrException(questionBoardCommentId);

        // 댓글 작성자 확인
        if (questionBoardComment.getUserAccount() != userAccount) {
                throw new DoubleSApplicationException(
                        ErrorCode.INVALID_PERMISSION, String.format(
                        "%s는 권한이 댓글 번호: '%s' 에 대해서 권한이 없습니다.",
                        userId,
                        questionBoardCommentId
                )
            );
        }

        questionBoardCommentRepository.delete(questionBoardComment);
    }

    private UserAccount getUserAccountOrException(String userId) {
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

    private QuestionBoardComment getQuestionBoardCommentOrException(Long questionBoardCommentId) {
        // find question board
        return questionBoardCommentRepository.findById(questionBoardCommentId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.COMMENT_NOT_FOUND, String.format("댓글번호: '%s' 를 찾지 못했습니다.", questionBoardCommentId))
        );
    }
}
