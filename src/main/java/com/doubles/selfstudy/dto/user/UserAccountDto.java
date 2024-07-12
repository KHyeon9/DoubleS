package com.doubles.selfstudy.dto.user;

import com.doubles.selfstudy.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAccountDto implements UserDetails {

    private String userId; // 유저 ID
    private String password; // 비밀번호
    private RoleType roleType; // roleType
    private String email; // 이메일
    private String nickname; // 닉네임
    private String memo; // 메로(자기 소개 비슷하게 사용)
    private LocalDateTime createdAt; // 생성 일시
    private String createdBy; // 생성자
    private LocalDateTime modifiedAt; // 수정 일시
    private String modifiedBy; // 수정자

    public static UserAccountDto of(String userId, String password, RoleType roleType, String email, String nickname, String memo) {
        return new UserAccountDto(userId, password, roleType, email, nickname, memo, null, null, null, null);
    }

    public static UserAccountDto of(String userId, String password, RoleType roleType, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new UserAccountDto(userId, password, roleType, email, nickname, memo, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static UserAccountDto fromEntity(UserAccount entity) {
        return new UserAccountDto(
                entity.getUserId(),
                entity.getPassword(),
                entity.getRoleType(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getMemo(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(roleType.getRoleName()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nickname;
    }
}
