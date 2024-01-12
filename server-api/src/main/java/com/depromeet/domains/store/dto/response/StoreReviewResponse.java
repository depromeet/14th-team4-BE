package com.depromeet.domains.store.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class StoreReviewResponse {

    private Long userId;
    private String nickName;
    private Integer rating;
    private String imageUrl;
    private Integer visitTimes;
    private LocalDate visitedAt;
    private String description;

    public static StoreReviewResponse of(Long userId, String nickName, Integer rating, String imageUrl, Integer visitTimes, LocalDate visitedAt, String description) {
        return StoreReviewResponse.builder()
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
