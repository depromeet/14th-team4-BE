package com.depromeet.domains.feed.entity;

import com.depromeet.enums.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FeedType implements EnumType{
    ALL("모든 피드"), FOLLOW("팔로우 피드");
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
