package com.depromeet.domains.store.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.depromeet.domains.review.entity.Review;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.user.entity.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewRequest {
	private Long storeId;
	private NewStoreRequest newStore;
	private Float rating;
	private String visitedAt;
	private String imageUrl;
	private String description;

	public Review toEntity(Store store, User user, Integer visitTimes) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
		LocalDate dateformat = LocalDate.parse(visitedAt, formatter);

		return Review.builder()
			.store(store)
			.user(user)
			.rating(this.rating)
			.visitedAt(dateformat)
			.visitTimes(visitTimes)
			.imageUrl(this.imageUrl)
			.description(this.description)
			.build();
	}
}
