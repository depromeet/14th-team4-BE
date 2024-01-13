package com.depromeet.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserLevel implements EnumType {
    LEVEL1("배고픈"),
    LEVEL2("맨밥이"),
    LEVEL3("또밥이"),
    LEVEL4("또또밥이");

    private final String description;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
