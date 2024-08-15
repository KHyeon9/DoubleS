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

    // 회원가입
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

    // 로그인
    public String login(String userId, String password) {
        // 회원 가입 체크
        UserAccount userAccount = getUserAccountOrException(userId);

        // 비밀 번호 체크
        if (!encoder.matches(password, userAccount.getPassword())) {
            throw new DoubleSApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        String token = JwtTokenUtils.createJwtToken(userId, secretKey, expiredTimeMs);

        return token;
    }
    
    // 유저 정보 조회
    public UserAccountDto getUserInfo(String userId) {
        // 아이디 가져옴
        UserAccount userAccount = getUserAccountOrException(userId);

        return UserAccountDto.fromEntity(userAccount);
    }

    // 유저 정보 수정
    @Transactional
    public UserAccountDto modifiyUserInfo(String userId, String nickname, String email, String memo) {
        // 유저 정보 가져옴
        UserAccount userAccount = getUserAccountOrException(userId);

        userAccount.setNickname(nickname);
        userAccount.setEmail(email);
        userAccount.setMemo(memo);
        
        // 변경 내용 수정
        return UserAccountDto.fromEntity(userAccountRepository.saveAndFlush(userAccount));
    }

    // 유저 비밀번호 수정
    public UserAccountDto modifiyUserPassword(String userId, String nowPassword, String changePassword) {
        // 유저 정보 가져옴
        UserAccount userAccount = getUserAccountOrException(userId);

        // 비밀 번호 체크
        if (!encoder.matches(nowPassword, userAccount.getPassword())) {
            throw new DoubleSApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 변경 내용 수정
        userAccount.setPassword(encoder.encode(changePassword));

        return UserAccountDto.fromEntity(userAccountRepository.saveAndFlush(userAccount));
    }

    private UserAccount getUserAccountOrException(String userId) {
        // 유저 정보 가져오면서 못 찾는 경우 검사
        return userAccountRepository.findById(userId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("유저 %s를 찾지 못했습니다.", userId))
        );
    }
    
    // 토큰 필터 설정에서 좀 더 편하게 사용할 수 있도록 작성
    public UserAccountDto loadUserByUserId(String userId) {
        return userAccountRepository.findById(userId).map(UserAccountDto::fromEntity).orElseThrow(
                () -> new DoubleSApplicationException(
                        ErrorCode.USER_NOT_FOUND, String.format("%s를 찾지 못했습니다", userId))
        );
    }
}
