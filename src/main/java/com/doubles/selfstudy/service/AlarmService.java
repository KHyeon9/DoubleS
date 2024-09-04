package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.alarm.AlarmDto;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.repository.AlarmRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final ServiceUtils serviceUtils;

    public Page<AlarmDto> alarmList(String userId, Pageable pageable) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        return alarmRepository.findAllByUserAccount(userAccount, pageable)
                .map(AlarmDto::fromEntity);
    }
}
