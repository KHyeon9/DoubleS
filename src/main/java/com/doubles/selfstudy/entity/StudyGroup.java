package com.doubles.selfstudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "study_group")
@Entity
public class StudyGroup extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "study_group_name")
    private String StudyGroupName;

    @Column(name = "description")
    private String description;

    protected StudyGroup() {}

    private StudyGroup(Long id, String studyGroupName, String description) {
        this.id = id;
        this.StudyGroupName = studyGroupName;
        this.description = description;
    }

    public static StudyGroup of(String studyGroupName, String description) {
        return new StudyGroup(null, studyGroupName, description);
    }

    public static StudyGroup of(Long id, String studyGroupName, String description) {
        return new StudyGroup(id, studyGroupName, description);
    }
}
