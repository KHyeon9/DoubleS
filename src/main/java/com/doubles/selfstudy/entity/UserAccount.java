package com.doubles.selfstudy.entity;

import com.doubles.selfstudy.dto.user.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@ToString(callSuper = true)
@Entity
public class UserAccount extends AuditingFields {

    @Id
    @Column(length = 50)
    private String userId; // 유저 ID

    @Setter
    @Column(nullable = false)
    private String password; // 비밀번호

    @Enumerated(EnumType.STRING)
    private RoleType roleType = RoleType.USER; // 일반적으로는 USER만 생성되도록 함.

    @Setter
    @Column(length = 100, nullable = false)
    private String email; // 이메일

    @Setter
    @Column(length = 100, nullable = false)
    private String nickname; // 닉네임

    @Setter
    private String memo; // 메로(자기 소개 비슷하게 사용)

    protected UserAccount() {}

    private UserAccount(String userId, String password, String email, String nickname, String memo, String createdBy) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
    }

    public static UserAccount of(String userId, String password, String email, String nickname, String memo) {
        return UserAccount.of(userId, password, email, nickname, memo, null);
    }

    public static UserAccount of(String userId, String password, String email, String nickname, String memo, String createdBy) {
        return new UserAccount(userId, password, email, nickname, memo, createdBy);
    }
}
