package com.depromeet.domains.store.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreReviewResponse {

	private Long userId;
	private String nickName;
	private Float rating;
	private String imageUrl;
	private Integer visitTimes;
	private LocalDateTime visitedAt;
	private String description;

	public static StoreReviewResponse of(Long userId, String nickName, Float rating, String imageUrl,
		Integer visitTimes, LocalDateTime visitedAt, String description) {
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
