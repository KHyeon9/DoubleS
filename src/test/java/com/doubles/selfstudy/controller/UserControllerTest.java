package com.doubles.selfstudy.controller;

import com.doubles.selfstudy.controller.request.UserLoginRequest;
import com.doubles.selfstudy.controller.request.UserInfoModifyRequest;
import com.doubles.selfstudy.controller.request.UserPasswordModifyRequest;
import com.doubles.selfstudy.controller.request.UserRegistRequest;
import com.doubles.selfstudy.controller.response.ProfileResponse;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.service.QuestionBoardService;
import com.doubles.selfstudy.service.UserAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @MockBean
    private QuestionBoardService questionBoardService;

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
                    .content(objectMapper.writeValueAsBytes(new UserRegistRequest(userId, nickname, email, password)))
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
        when(userAccountService.regist(userId, password , email, nickname))
                .thenThrow(new DoubleSApplicationException(ErrorCode.DUPLICATED_USER_ID));

        // Then
        mockMvc.perform(post("/api/regist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserRegistRequest(userId, nickname, email, password)))
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
                .thenThrow(new DoubleSApplicationException(ErrorCode.DUPLICATED_USER_ID));

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
                .thenThrow(new DoubleSApplicationException(ErrorCode.INVALID_PASSWORD));

        // Then
        mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userId, password)))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PASSWORD.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 회원_정보_조회_성공() throws Exception {
        // Given
        UserAccountDto userAccountDto =  UserAccountDto
                .fromEntity(UserAccountFixture.get("userId", "password"));

        // When
        when(userAccountService.getUserInfo(any()))
                .thenReturn(userAccountDto);
        when(questionBoardService.profileQuestionBoardList(any()))
                .thenReturn(List.of());

        // Then
        mockMvc.perform(get("/api/main/profile/userId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsBytes(ProfileResponse
                                .fromUserAccountDtoAndQuestionBoardListDto(userAccountDto, List.of())
                        )
                    )
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 회원_정보_조회시_로그인이_안된_경우_에러_발생() throws Exception {
        // Given

        // When&Then
        mockMvc.perform(get("/api/main/profile/userId")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 회원_정보_수정_성공() throws Exception {
        // Given
        String email = "test@email.com";
        String nickname = "nickname";
        String memo = "memo";

        // When
        when(userAccountService.modifiyUserInfo(any(), eq(email), eq(nickname), eq(memo)))
                .thenReturn(mock(UserAccountDto.class));

        // Then
        mockMvc.perform(put("/api/main/profile/user_info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserInfoModifyRequest(email, nickname, memo))
                        )
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 회원_정보_수정시_로그인이_안된_경우_에러_발생() throws Exception {
        // Given
        String email = "test@email.com";
        String nickname = "nickname";
        String memo = "memo";

        // When
        when(userAccountService.modifiyUserInfo(any(), eq(email), eq(nickname), eq(memo)))
                .thenReturn(mock(UserAccountDto.class));

        // Then
        mockMvc.perform(put("/api/main/profile/user_info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserInfoModifyRequest(email, nickname, memo))
                        )
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 회원_비밀번호_수정_성공() throws Exception {
        // Given
        String nowPassword = "nowPassword";
        String changePassword = "changePassword";

        // When
        when(userAccountService.modifiyUserPassword(any(), eq(nowPassword), eq(changePassword)))
                .thenReturn(mock(UserAccountDto.class));;

            // Then
            mockMvc.perform(put("/api/main/profile/user_password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(new UserPasswordModifyRequest(nowPassword, changePassword))
                        )
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 회원_비밀번호_수정시_로그인하지_않은_경우_에러_발생() throws Exception {
        // Given
        String nowPassword = "nowPassword";
        String changePassword = "changePassword";

        // When
        when(userAccountService.modifiyUserPassword(any(), eq(nowPassword), eq(changePassword)))
                .thenReturn(mock(UserAccountDto.class));;

        // Then
        mockMvc.perform(put("/api/main/profile/user_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserPasswordModifyRequest(nowPassword, changePassword))
                        )
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 유저_삭제_성공() throws Exception {
        // Given

        // When&Then
        mockMvc.perform(delete("/api/main/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 유저_삭제시_로그인_안한_경우_에러_발생() throws Exception {
        // Given

        // When&Then
        mockMvc.perform(delete("/api/main/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }
}