package com.doubles.selfstudy.dto.todo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImportanceType {

    High("높음"),
    Middle("중간"),
    Low("낮음");

    private final String typeName;

    public static ImportanceType fromString(String typeName) {
        for (ImportanceType importanceType : ImportanceType.values()) {
            if (importanceType.name().equalsIgnoreCase(typeName)) {
                return importanceType;
            }
        }

        throw new IllegalArgumentException("Type을 찾지 못했습니다 type: " + typeName);
    }
}
