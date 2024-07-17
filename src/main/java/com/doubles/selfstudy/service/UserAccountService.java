package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.UserAccountRepository;
import com.doubles.selfstudy.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    @Transactional
    public UserAccountDto regist(String userId, String password, String email, String nickname) {
        // 회원 가입하는 userId 중복 체크
        userAccountRepository.findById(userId).ifPresent(it -> {
            throw new DoubleSApplicationException(ErrorCode.DUPLICATED_USER_ID, String.format("'%s' 는 있는 아이디입니다.", userId));
        });

        // 회원 가입 진행
        UserAccount userAccount = userAccountRepository.save(
                UserAccount.of(userId, encoder.encode(password), email, nickname, null));

        return UserAccountDto.fromEntity(userAccount);
    }

    public String login(String userId, String password) {
        // 회원 가입 체크
        UserAccount userAccount = userAccountRepository.findById(userId).orElseThrow(() ->
                new DoubleSApplicationException(
                        ErrorCode.USER_NOT_FOUND, String.format("'%s' 를 찾지 못했습니다.", userId))
        );

        // 비밀 번호 체크
        if (!encoder.matches(password, userAccount.getPassword())) {
            throw new DoubleSApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        String token = JwtTokenUtils.createJwtToken(userId, secretKey, expiredTimeMs);

        return token;
    }
    
    // 토큰 필터 설정에서 좀 더 편하게 사용할 수 있도록 작성
    public UserAccountDto loadUserByUserId(String userId) {
        return userAccountRepository.findById(userId).map(UserAccountDto::fromEntity).orElseThrow(
                () -> new DoubleSApplicationException(
                        ErrorCode.USER_NOT_FOUND, String.format("%s를 찾지 못했습니다", userId))
        );
    }
}
