package com.depromeet.domains.store.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StoreLogResponse {

    private Long userId;
    private String nickName;
    private Float rating;
    private String imageUrl;
    private Integer visitTimes;
    private LocalDateTime visitedAt;
    private String description;

    public static StoreLogResponse of(Long userId, String nickName, Float rating, String imageUrl, Integer visitTimes, LocalDateTime visitedAt, String description) {
        return StoreLogResponse.builder()
                .userId(userId)
                .nickName(nickName)
                .rating(rating)
                .imageUrl(imageUrl)
                .visitTimes(visitTimes)
                .visitedAt(visitedAt)
                .description(description)
                .build();
    }

}
