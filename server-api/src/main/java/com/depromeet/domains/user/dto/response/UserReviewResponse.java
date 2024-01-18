package com.depromeet.domains.user.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserReviewResponse {
	private Long reviewId;
	private Long storeId;
	private String storeName;
	private Integer visitTimes;
	@JsonFormat(pattern = "yyyy.MM.dd")
	private LocalDate visitedAt;
	private String categoryName;
	private Integer rating;
	private String imageUrl;
	private String description;

	public static UserReviewResponse of(Long reviewId, Long storeId, String storeName, Integer visitTimes, LocalDate visitedAt,
		String categoryName,
		Integer rating, String imageUrl, String description) {
		return UserReviewResponse.builder()
			.reviewId(reviewId)
			.storeId(storeId)
			.storeName(storeName)
			.visitTimes(visitTimes)
			.visitedAt(visitedAt)
			.categoryName(categoryName)
			.rating(rating)
			.imageUrl(imageUrl)
			.description(description)
			.build();
	}
}
