package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.doubles.selfstudy.fixture.UserAccountFixture.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserAccountServiceTest {

    @Autowired
    private UserAccountService userAccountService;
    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입이_정상적으로_작동하는_경우() {
        // Given
        String userId = "userId";
        String password = "password";
        String email = "test@email.com";
        String nickname = "nickname";

        UserAccount fixture = get(userId, password, email, nickname);

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userAccountRepository.save(any())).thenReturn(fixture);

        // Then
        assertDoesNotThrow(() -> userAccountService.regist(userId, password, email, nickname));
    }

    @Test
    void 회원가입시_아이디가_중복되면_에러를_반환() {
        // Given
        String userId = "userId";
        String password = "password";
        String email = "test@email.com";
        String nickname = "nickname";

        UserAccount fixture = get(userId, password, email, nickname);

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userAccountRepository.save(any())).thenReturn(fixture);

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class, () -> userAccountService.regist(userId, password, email, nickname));
        assertEquals(ErrorCode.DUPLICATED_USER_ID, e.getErrorCode());
    }

    @Test
    void 로그인이_정상적으로_작동하는_경우() {
        // Given
        String userId = "userId";
        String password = "password";
        UserAccount fixture = get(userId, password);

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        // Then
        assertDoesNotThrow(() -> userAccountService.login(userId, password));
    }

    @Test
    void 로그인시_userId로_가입한_유저가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());

        // Then
        assertThrows(DoubleSApplicationException.class, () -> userAccountService.login(userId, password));
    }

    @Test
    void 로그인시_password가_틀린_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";
        String wrongPassword = "wrongPassword";
        UserAccount fixture = get(userId, password);

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(fixture));

        // Then
        assertThrows(DoubleSApplicationException.class, () -> userAccountService.login(userId, wrongPassword));
    }

    @Test
    void 유저_정보_조회_성공() {
        // Given
        String userId = "userId";
        String password = "password";
        UserAccount fixture = get(userId, password);

        // When
        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(fixture));

        // Then
        assertDoesNotThrow(() -> userAccountService.getUserInfo(userId));
    }
}