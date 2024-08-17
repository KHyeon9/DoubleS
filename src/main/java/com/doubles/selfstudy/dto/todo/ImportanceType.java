package com.doubles.selfstudy.dto.todo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImportanceType {

    high("높음"),
    middle("중간"),
    low("낮음");

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
