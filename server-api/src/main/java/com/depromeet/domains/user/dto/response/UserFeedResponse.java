package com.depromeet.domains.user.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserFeedResponse {
	private Long feedId;
	private Long storeId;
	private String storeName;
	private Integer visitTimes;
	@JsonFormat(pattern = "yyyy.MM.dd")
	private LocalDate visitedAt;
	private String categoryName;
	private Integer rating;
	private String imageUrl;
	private String description;

	public static UserFeedResponse of(Long feedId, Long storeId, String storeName, Integer visitTimes, LocalDate visitedAt,
		String categoryName,
		Integer rating, String imageUrl, String description) {
		return UserFeedResponse.builder()
			.feedId(feedId)
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
