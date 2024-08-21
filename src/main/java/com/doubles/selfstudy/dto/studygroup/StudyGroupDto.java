package com.doubles.selfstudy.dto.studygroup;

import com.doubles.selfstudy.entity.StudyGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudyGroupDto {

    private Long id;
    private String StudyGroupName;
    private String description;

    public static StudyGroupDto of(String studyGroupName, String description) {
        return StudyGroupDto.of(null, studyGroupName, description);
    }

    public static StudyGroupDto of(Long id, String studyGroupName, String description) {
        return new StudyGroupDto(id, studyGroupName, description);
    }

    public static StudyGroupDto fromEntity(StudyGroup entity) {
        return new StudyGroupDto(
                entity.getId(),
                entity.getStudyGroupName(),
                entity.getDescription()
        );
    }
}
