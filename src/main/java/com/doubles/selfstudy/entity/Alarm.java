package com.doubles.selfstudy.entity;

import com.doubles.selfstudy.dto.alarm.AlarmType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(
        name = "`alarm`",
        indexes = {
                @Index(
                        name = "user_id_idx",
                        columnList = "user_id"
                )
        }
)
@Entity
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 알람 받을 사람
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column(name = "from_user_id")
    private String fromUserId;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "data")
    private String data;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected Alarm() {}

    private Alarm(UserAccount userAccount, AlarmType alarmType, String fromUserId, Long targetId, String data, LocalDateTime createdAt) {
        this.userAccount = userAccount;
        this.alarmType = alarmType;
        this.fromUserId = fromUserId;
        this.targetId = targetId;
        this.data = data;
        this.createdAt = createdAt;
    }

    public static Alarm of(UserAccount userAccount, AlarmType alarmType, String fromUserId, Long targetId, String data) {
        return Alarm.of(
                userAccount,
                alarmType,
                fromUserId,
                targetId,
                data,
                null
        );
    }

    public static Alarm of(UserAccount userAccount, AlarmType alarmType, String fromUserId, Long targetId, String data, LocalDateTime createdAt) {
        return new Alarm(
                userAccount,
                alarmType,
                fromUserId,
                targetId,
                data,
                createdAt
        );
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
