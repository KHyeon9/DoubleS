package com.doubles.selfstudy.dto.alarm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {

    NEW_COMMENT_ON_POST,
    NEW_LIKE_ON_POST,
    NEW_COMMENT_ON_STUDY_GROUP_POST,
    INVITE_STUDY_GROUP,
    NEW_CHAT_MESSAGE,
    ;

}
