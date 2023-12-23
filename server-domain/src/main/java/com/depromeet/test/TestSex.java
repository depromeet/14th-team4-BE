package com.depromeet.test;

import com.depromeet.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TestSex implements EnumType {
    MALE("남자"),
    FEMALE("여자");

    private String description;

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
