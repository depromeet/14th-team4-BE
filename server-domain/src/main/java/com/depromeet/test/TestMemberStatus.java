package com.depromeet.test;

import com.depromeet.enums.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TestMemberStatus implements EnumType {

    LOCK("일시 정지"),
    NORMAL("정상"),
    BAN("영구 정지");

    private final String description;

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
