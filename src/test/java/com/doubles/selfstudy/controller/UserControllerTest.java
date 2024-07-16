package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.UserLoginRequest;
import com.doubles.selfstudy.controller.request.UserRegistRequest;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.service.UserAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserAccountService userAccountService;

    @Test
    void 회원가입_정상_작동() throws Exception {
        // Given
        String userId = "userId";
        String password = "password";
        String email = "test@email.com";
        String nickname = "nickname";

        // When
        when(userAccountService.regist(userId, password, email, nickname))
                .thenReturn(mock(UserAccountDto.class));

        // Then
        mockMvc.perform(post("/api/regist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserRegistRequest(userId, password, email, nickname)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 회원가입시_이미_있는_userId로_가입을_하는_경우() throws Exception {
        // Given
        String userId = "userId";
        String password = "password";
        String email = "test@email.com";
        String nickname = "nickname";

        // When
        when(userAccountService.regist(userId, password, email, nickname))
                .thenThrow(new DoubleSApplicationException(ErrorCode.DUPLICATED_USER_ID, ""));

        // Then
        mockMvc.perform(post("/api/regist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserRegistRequest(userId, password, email, nickname)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.DUPLICATED_USER_ID.getStatus().value()));
    }

    @Test
    void 로그인_정상_작동() throws Exception {
        // Given
        String userId = "userId";
        String password = "password";

        // When
        when(userAccountService.login(userId, password)).thenReturn("test_token");

        // Then
        mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userId, password)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 로그인시_userId로_가입한_유저가_없는_경우_에러_반환() throws Exception {
        // Given
        String userId = "userId";
        String password = "password";

        // When
        when(userAccountService.login(userId, password))
                .thenThrow(new DoubleSApplicationException(ErrorCode.DUPLICATED_USER_ID, ""));

        // Then
        mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userId, password)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.DUPLICATED_USER_ID.getStatus().value()));
    }

    @Test
    void 로그인시_password가_틀린_경우_에러_반환() throws Exception {
        // Given
        String userId = "userId";
        String password = "password";

        // When
        when(userAccountService.login(userId, password))
                .thenThrow(new DoubleSApplicationException(ErrorCode.INVALID_PASSWORD, ""));

        // Then
        mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userId,password)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PASSWORD.getStatus().value()));
    }
}