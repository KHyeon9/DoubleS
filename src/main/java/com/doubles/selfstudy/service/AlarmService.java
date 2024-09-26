package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.alarm.AlarmDto;
import com.doubles.selfstudy.dto.alarm.AlarmType;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.repository.AlarmRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final ServiceUtils serviceUtils;

    public Page<AlarmDto> alarmList(String userId, Pageable pageable) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 나의 알람 가져오기
        Page<Object[]> results = alarmRepository.findAlarmTypesAndCountsByUserAccount(userAccount, pageable);

        return results.map(result -> AlarmDto.fromEntity(
                (AlarmType) result[0], (Long) result[1], (String) result[2], (Long) result[3]));
    }

    // 유저의 알람 삭제
    @Transactional
    public void deleteAlarm(String userId, Long targetId, AlarmType alarmType) {
        // 유저
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 삭제
        alarmRepository.deleteByUserAccountAndTargetIdAndAlarmType(userAccount, targetId, alarmType);
    }
}
