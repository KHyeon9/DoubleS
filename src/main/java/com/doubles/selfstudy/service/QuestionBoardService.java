package com.doubles.selfstudy.service;

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

    @Transactional
    public void createPost(String title, String content, String userId) {
        // user find
        UserAccount userAccount = getUserAccountOrException(userId);

        // post save
        questionBoardRepository.save(
                QuestionBoard.of(userAccount, title, content)
        );
    }

    private UserAccount getUserAccountOrException(String userId) {
        // find useraccount
        return userAccountRepository.findById(userId).orElseThrow(() ->
                    new DoubleSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s를 찾지 못했습니다.", userId))
                );
    }
}
