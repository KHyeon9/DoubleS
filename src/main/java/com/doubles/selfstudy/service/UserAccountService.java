package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountDto regist(String userId, String password, String email, String nickname) {
        // 회원가입하는 userId 중복 체크
        userAccountRepository.findById(userId).ifPresent(it -> {
            throw new DoubleSApplicationException(ErrorCode.DUPLICATED_USER_ID, String.format("%s is duplicated", userId));
        });

        // 회원가입 진행
        UserAccount userAccount = userAccountRepository.save(UserAccount.of(userId, password, email, nickname, null));

        return UserAccountDto.fromEntity(userAccount);
    }
}
