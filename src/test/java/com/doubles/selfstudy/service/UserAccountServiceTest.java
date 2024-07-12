package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(fixture); // 수정된 부분

        // Then
        assertDoesNotThrow(() -> userAccountService.regist(userId, password, email, nickname));
    }
}