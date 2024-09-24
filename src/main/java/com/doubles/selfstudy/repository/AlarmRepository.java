package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.dto.alarm.AlarmType;
import com.doubles.selfstudy.entity.Alarm;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    // 알람 조회
    @Query("SELECT a.alarmType, a.targetId, a.data, COUNT(a) as alarmCount " +
            "FROM Alarm a " +
            "WHERE a.userAccount = :userAccount " +
            "GROUP BY a.alarmType, a.targetId, a.data")
    Page<Object[]> findAlarmTypesAndCountsByUserAccount(@Param("userAccount") UserAccount userAccount, Pageable pageable);

    // 알람 중복이 없어야 하는 경우 존재하는지 조회를 위한 메소드 
    boolean existsByUserAccountAndFromUserIdAndAlarmType(UserAccount user, String fromUserId, AlarmType alarmType);
    
    // 알람 삭제
    void deleteByTargetIdAndAlarmType(Long targetId, AlarmType alarmType);
}
