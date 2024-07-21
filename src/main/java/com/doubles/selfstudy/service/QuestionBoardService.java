package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.post.QuestionBoardDto;
import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.QuestionBoardRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class QuestionBoardService {

    private final UserAccountRepository userAccountRepository;
    private final QuestionBoardRepository questionBoardRepository;
    
    // 질문 게시글 리스트
    public Page<QuestionBoardDto> questionBoardList(Pageable pageable) {
        return questionBoardRepository.findAll(pageable).map(QuestionBoardDto::fromEntity);
    }

    // 나의 질문 게시글 리스트
    public Page<QuestionBoardDto> myQuestionBoardList(String userId, Pageable pageable) {
        UserAccount userAccount = getUserAccountOrException(userId);

        return questionBoardRepository.findByUserAccount(userAccount, pageable).map(QuestionBoardDto::fromEntity);
    }

    // 질문 게시글 생성
    @Transactional
    public void createQuestionBoard(String title, String content, String userId) {
        // user find
        UserAccount userAccount = getUserAccountOrException(userId);

        // question board save
        questionBoardRepository.save(
                QuestionBoard.of(userAccount, title, content)
        );
    }

    // 질문 게시글 수정
    @Transactional
    public QuestionBoardDto modifyQuestionBoard(String title, String content, String userId, Long questionBoardId) {
        // user find
        UserAccount userAccount = getUserAccountOrException(userId);

        // question board find
        QuestionBoard questionBoard = getQuestionBoardOrException(questionBoardId);

        // question board permission
        if (questionBoard.getUserAccount() != userAccount) {
                throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                            "%s는 권한이 게시판 번호: '%s' 에 대해서 권한이 없습니다.",
                            userId,
                            questionBoardId
                    )
                );
        }

        questionBoard.setTitle(title);
        questionBoard.setContent(content);

        return QuestionBoardDto.fromEntity(questionBoardRepository.saveAndFlush(questionBoard));
    }

    // 질문 게시글 삭제
    @Transactional
    public void deleteQuestionBoard(String userId, Long questionBoardId) {
        // user find
        UserAccount userAccount = getUserAccountOrException(userId);

        // question board find
        QuestionBoard questionBoard = getQuestionBoardOrException(questionBoardId);

        // question board permission
        if (questionBoard.getUserAccount() != userAccount) {
                throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                        "%s는 권한이 게시판 번호: '%s' 에 대해서 권한이 없습니다.",
                        userId,
                        questionBoardId
                    )
                );
        }

        questionBoardRepository.delete(questionBoard);
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
