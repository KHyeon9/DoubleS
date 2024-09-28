package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.alarm.AlarmDto;
import com.doubles.selfstudy.dto.alarm.AlarmType;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.AlarmRepository;
import com.doubles.selfstudy.repository.EmitterRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final EmitterRepository emitterRepository;
    private final ServiceUtils serviceUtils;

    private final static Long DEFAULT_TIME_OUT = 60L * 1000 * 60;
    private final static String ALARM_NAME = "alarm";

    public Page<AlarmDto> alarmList(String userId, Pageable pageable) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 나의 알람 가져오기
        Page<Object[]> results = alarmRepository.findAlarmTypesAndCountsByUserAccount(userAccount, pageable);

        return results.map(result -> AlarmDto.fromEntity(
                (AlarmType) result[0], (Long) result[1], (String) result[2], (Long) result[3]));
    }

    // 탑 네비 알람 조회
    public List<AlarmDto> topNavAlarmList(String userId) {
        // 유저 조회
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 알람 조회
        List<AlarmDto> alarmList = new ArrayList<>();

        for (AlarmType alarmType : AlarmType.values()) {
            Long alarmCount = alarmRepository.countByUserAccountAndAlarmType(userAccount, alarmType);

            // 알람이 있을 경우만 add
            if (alarmCount > 0) {
                alarmList.add(
                        AlarmDto.fromEntity(
                                alarmType,
                                alarmCount
                        )
                );
            }
        }

        return alarmList;
    }

    // 알람 sse connect
    public SseEmitter connectAlarm(String userId) {
        // 유저 검사
        serviceUtils.getUserAccountOrException(userId);

        // sse 셋팅 및 연결
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIME_OUT);
        emitterRepository.save(userId, sseEmitter);

        // 완료된 경우
        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        // 타임 아웃된 경우
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            sseEmitter.send(SseEmitter.event().id("").name(ALARM_NAME).data("connect completed"));
        } catch (IOException e) {
            throw new DoubleSApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
        }

        return sseEmitter;
    }
    
    // 알람에 저장 되었을 때, 저장됨을 알려주는 메소드
    public void alarmSend(Long alarmId, String userId) {
        emitterRepository.get(userId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(alarmId.toString()).name(ALARM_NAME).data("new alarm"));
            } catch (IOException e) {
                emitterRepository.delete(userId);
                throw new DoubleSApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
            }
        }, () -> log.info("emitter를 찾지 못했습니다."));
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
