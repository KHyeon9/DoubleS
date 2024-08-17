package com.doubles.selfstudy.utils;

import com.doubles.selfstudy.dto.user.RoleType;
import com.doubles.selfstudy.entity.NoticeBoard;
import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardComment;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.NoticeBoardRepository;
import com.doubles.selfstudy.repository.QuestionBoardCommentRepository;
import com.doubles.selfstudy.repository.QuestionBoardRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class ServiceUtils {

    private final UserAccountRepository userAccountRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final NoticeBoardRepository noticeBoardRepository;

    public UserAccount getUserAccountOrException(String userId) {
        // 유저 정보 가져오면서 못 찾는 경우 검사
        return userAccountRepository.findById(userId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("유저 %s를 찾지 못했습니다.", userId))
        );
    }

    public UserAccount getAdminUserAccountOrException(String userId) {
        UserAccount userAccount = userAccountRepository.findById(userId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("유저 %s를 찾지 못했습니다.", userId)
                )
        );

        // 유저 권한 확인
        if (!Objects.equals(userAccount.getRoleType().getRoleName(), RoleType.ADMIN.getRoleName())) {
            throw new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s는 관리자가 아닙니다.", userId));
        }

        return userAccount;
    }

    public QuestionBoard getQuestionBoardOrException(Long questionBoardId) {
        // 질문 게시글 가져오면서 못 찾는 경우 검사
        return questionBoardRepository.findById(questionBoardId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND, String.format("게시글 번호: '%s' 를 찾지 못했습니다.", questionBoardId))
        );
    }

    public QuestionBoardComment getQuestionBoardCommentOrException(Long questionBoardCommentId) {
        // find question board
        return questionBoardCommentRepository.findById(questionBoardCommentId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.COMMENT_NOT_FOUND, String.format("댓글번호: '%s' 를 찾지 못했습니다.", questionBoardCommentId))
        );
    }

    public NoticeBoard getNoticeBoardOrException(Long noticeBoardId) {
        // 공지사항 글을 가져오면서 못 찾는 경우 검사
        return noticeBoardRepository.findById(noticeBoardId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND, String.format("공지사항 %d번을 찾지 못했습니다.", noticeBoardId)
                )
        );
    }
}
