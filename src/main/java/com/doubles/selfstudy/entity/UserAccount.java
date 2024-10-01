package com.doubles.selfstudy.entity;

import com.doubles.selfstudy.dto.user.RoleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "user_account")
@ToString(callSuper = true)
@Entity
public class UserAccount {

    @Id
    @Column(length = 50, name = "user_id")
    private String userId; // 유저 ID

    @Column(nullable = false, name = "password")
    private String password; // 비밀번호

    @Column(name = "role_type")
    @Enumerated(EnumType.STRING)
    private RoleType roleType = RoleType.USER; // 일반적으로는 USER만 생성되도록 함.

    @Column(length = 100, nullable = false, name = "email")
    private String email; // 이메일

    @Column(length = 100, nullable = false, name = "nickname")
    private String nickname; // 닉네임

    @Column(name = "memo")
    private String memo; // 메모 (자기 소개 비슷하게 사용)

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    protected UserAccount() {}

    private UserAccount(String userId, String password, RoleType roleType, String email, String nickname, String memo, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.userId = userId;
        this.password = password;
        this.roleType = roleType;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static UserAccount of(String userId, String password, String email, String nickname, String memo) {
        return UserAccount.of(userId, password, RoleType.USER, email, nickname, memo, null, null);
    }

    public static UserAccount of(String userId, String password, RoleType roleType, String email, String nickname, String memo) {
        return UserAccount.of(userId, password, roleType, email, nickname, memo, null, null);
    }

    public static UserAccount of(String userId, String password, RoleType roleType, String email, String nickname, String memo, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new UserAccount(userId, password, roleType, email, nickname, memo, createdAt, modifiedAt);
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
