package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.question.QuestionBoardCommentDto;
import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardComment;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.QuestionBoardCommentRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class QuestionBoardCommentService {

    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final ServiceUtils serviceUtils;

    // 질문 게시글 댓글 리스트 조회
    public Page<QuestionBoardCommentDto> questionBoardCommentList(Long questionBoardId, Pageable pageable) {
        // 질문 게시글 확인
        QuestionBoard questionBoard = serviceUtils.getQuestionBoardOrException(questionBoardId);

        return questionBoardCommentRepository
                .findAllByQuestionBoard(questionBoard, pageable)
                .map(QuestionBoardCommentDto::fromEntity);
    }

    // 질문 게시글 댓글 작성 기능
    @Transactional
    public void createQuestionBoardComment(String userId, Long questionBoardId, String comment) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 질문 게시글 확인
        QuestionBoard questionBoard =
                serviceUtils.getQuestionBoardOrException(questionBoardId);

        // 댓글 저장
        questionBoardCommentRepository.save(QuestionBoardComment.of(comment, userAccount, questionBoard));
    }

    // 질문 게시글 댓글 수정
    public QuestionBoardCommentDto modifyQuestionBoardComment(String userId, Long questionBoardId, Long questionBoardCommentId,  String comment) {
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 게시글 확인
        serviceUtils.getQuestionBoardOrException(questionBoardId);

        // 댓글 확인
        QuestionBoardComment questionBoardComment =
                serviceUtils.getQuestionBoardCommentOrException(questionBoardCommentId);

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

        return QuestionBoardCommentDto
                .fromEntity(questionBoardCommentRepository.saveAndFlush(questionBoardComment));
    }

    // 질문 게시글 댓글 삭제
    public void deleteQuestionBoardComment(String userId, Long questionBoardId, Long questionBoardCommentId) {
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 게시글 확인
        serviceUtils.getQuestionBoardOrException(questionBoardId);

        // 댓글 확인
        QuestionBoardComment questionBoardComment =
                serviceUtils.getQuestionBoardCommentOrException(questionBoardCommentId);

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
}
