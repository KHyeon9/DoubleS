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
public class UserAccount extends AuditingFields implements UserDetails {

    @Id
    @Column(length = 50)
    private String userId;

    @Setter
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType roleType = RoleType.USER;

    @Setter
    @Column(length = 100)
    private String email;

    @Setter
    @Column(length = 100)
    private String nickname;

    @Setter
    private String memo;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRoleType().getRoleName()));
    }

    @Override
    public String getUsername() {
        return getUserId();
    }
}
