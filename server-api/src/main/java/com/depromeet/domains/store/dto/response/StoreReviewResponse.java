package com.depromeet.domains.store.dto.response;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class StoreReviewResponse {

    private Long userId;
    private Long reviewId;
    private String nickName;
    private Integer rating;
    private String imageUrl;
    private Integer visitTimes;
    private LocalDate visitedAt;
    private String description;
	private Boolean isMine;

    public static StoreReviewResponse of(Long userId, Long reviewId, String nickName, Integer rating, String imageUrl,
        Integer visitTimes, LocalDate visitedAt, String description, Boolean isMine) {
        return StoreReviewResponse.builder()
                .userId(userId)
                .reviewId(reviewId)
                .nickName(nickName)
                .rating(rating)
                .imageUrl(imageUrl)
                .visitTimes(visitTimes)
                .visitedAt(visitedAt)
                .description(description)
                .isMine(isMine)
                .build();
    }

}
