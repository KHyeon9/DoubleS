package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.alarm.AlarmType;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.repository.AlarmRepository;
import com.doubles.selfstudy.repository.UserAccountCacheRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
class AlarmServiceTest {

    @Autowired
    private AlarmService alarmService;

    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private UserAccountCacheRepository userAccountCacheRepository;
    @MockBean
    private AlarmRepository alarmRepository;

    @Test
    void 알람_삭제_성공() {
        // Given
        String userId = "userId";
        Long targetId = 1L;
        AlarmType alarmType = AlarmType.NEW_CHAT_MESSAGE;
        UserAccount userAccount = UserAccountFixture.get(userId, "password");

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));

        // Then
        assertDoesNotThrow(() -> alarmService.deleteAlarm(userId, targetId, alarmType));
    }

    @Test
    void 알람_삭제시_로그인을_안한_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long targetId = 1L;
        AlarmType alarmType = AlarmType.NEW_CHAT_MESSAGE;

        // When
        when(userAccountCacheRepository.getUserAccount(userId))
                .thenReturn(Optional.empty());
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> alarmService.deleteAlarm(userId, targetId, alarmType)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 알람_리스트_조회_성공() {
        // Given
        String userId = "userId";
        Pageable pageable = mock(Pageable.class);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(alarmRepository.findAlarmTypesAndCountsByUserAccount(any(), any()))
                .thenReturn(Page.empty());

        // Then
        assertDoesNotThrow(() -> alarmService.alarmList(userId, pageable));
    }

    @Test
    void 탑_네비_알람_리스트_조회() {
        // Given
        String userId = "userId";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(alarmRepository.countByUserAccountAndAlarmType(any(), any()))
                .thenReturn(1L);

        // Then
        assertDoesNotThrow(() -> alarmService.topNavAlarmList(userId));
    }
}