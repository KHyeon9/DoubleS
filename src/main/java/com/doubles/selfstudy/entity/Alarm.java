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

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    protected Alarm() {}

    private Alarm(UserAccount userAccount, AlarmType alarmType, String fromUserId, Long targetId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.userAccount = userAccount;
        this.alarmType = alarmType;
        this.fromUserId = fromUserId;
        this.targetId = targetId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static Alarm of(UserAccount userAccount, AlarmType alarmType, String fromUserId, Long targetId) {
        return Alarm.of(
                userAccount,
                alarmType,
                fromUserId,
                targetId,
                null,
                null
        );
    }

    public static Alarm of(UserAccount userAccount, AlarmType alarmType, String fromUserId, Long targetId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new Alarm(
                userAccount,
                alarmType,
                fromUserId,
                targetId,
                createdAt,
                modifiedAt
        );
    }

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void modifiedAt() {
        this.modifiedAt = LocalDateTime.now();
    }
}
