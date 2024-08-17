package com.doubles.selfstudy.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImportanceTypeResponse {

    private final String key;
    private final String value;

    public static ImportanceTypeResponse of(String key, String value) {
        return new ImportanceTypeResponse(key, value);
    }
}
