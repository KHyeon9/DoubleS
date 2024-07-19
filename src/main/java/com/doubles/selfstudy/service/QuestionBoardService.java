package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.post.QuestionBoardDto;
import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.QuestionBoardRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class QuestionBoardService {

    private final UserAccountRepository userAccountRepository;
    private final QuestionBoardRepository questionBoardRepository;

    // 질문 게시글 생성
    @Transactional
    public void createQuestionBoard(String title, String content, String userId) {
        // user find
        UserAccount userAccount = getUserAccountOrException(userId);

        // post save
        questionBoardRepository.save(
                QuestionBoard.of(userAccount, title, content)
        );
    }

    // 질문 게시글 수정
    @Transactional
    public QuestionBoardDto modifyQuestionBoard(String title, String content, String userId, Long questionBoardId) {
        // user find
        UserAccount userAccount = getUserAccountOrException(userId);

        // post exist
        QuestionBoard questionBoard = getQuestionBoardOrException(questionBoardId);

        // post permission
        if (questionBoard.getUserAccount() != userAccount) {
                throw new DoubleSApplicationException(
                        ErrorCode.INVALID_PERMISSION, String.format("%s는 권한이 게시판 번호: '%s' 에 대해서 권한이 없습니다.",
                        userId, 
                        questionBoardId
                )
            );
        }

        questionBoard.setTitle(title);
        questionBoard.setContent(content);

        return QuestionBoardDto.fromEntity(questionBoardRepository.saveAndFlush(questionBoard));
    }

    private UserAccount getUserAccountOrException(String userId) {
        // find useraccount
        return userAccountRepository.findById(userId).orElseThrow(() ->
                    new DoubleSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("유저 %s를 찾지 못했습니다.", userId))
                );
    }

    private QuestionBoard getQuestionBoardOrException(Long questionBoardId) {
        return questionBoardRepository.findById(questionBoardId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND, String.format("게시글 번호: '%s' 를 찾지 못했습니다.", questionBoardId))
        );
    }
}
