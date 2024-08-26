package com.doubles.selfstudy.entity;

import com.doubles.selfstudy.dto.studygroup.StudyGroupPosition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "user_study_group")
@Entity
public class UserStudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private StudyGroupPosition position;

    @ManyToOne
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

    @Column(name = "joinedAt")
    private LocalDateTime joinedAt;

    protected UserStudyGroup() {}

    private UserStudyGroup(Long id, UserAccount userAccount, StudyGroupPosition position, StudyGroup studyGroup) {
        this.id = id;
        this.userAccount = userAccount;
        this.position = position;
        this.studyGroup = studyGroup;
    }

    public static UserStudyGroup of(UserAccount userAccount, StudyGroupPosition position, StudyGroup studyGroup) {
        return new UserStudyGroup(null, userAccount, position, studyGroup);
    }

    public static UserStudyGroup of(Long id, UserAccount userAccount, StudyGroupPosition position, StudyGroup studyGroup) {
        return new UserStudyGroup(id, userAccount, position, studyGroup);
    }

    @PrePersist
    void joinedAt() {
        this.joinedAt = LocalDateTime.now();
    }
}
