package com.depromeet.test;

import com.depromeet.enums.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TestEnumType implements EnumType {
    Good("좋음"), Bad("나쁨");

    private final String description;

    @Override
    public String getName() {
        return this.description;
    }

    @Override
    public String getDescription() {
        return this.name();
    }
}
