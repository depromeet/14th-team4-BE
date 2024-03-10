package com.depromeet.domains.store.dto.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.depromeet.annotation.VisitedDateFormat;
import com.depromeet.domains.feed.entity.Feed;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.user.entity.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedRequest {
	private Long storeId;
	@Valid
	private NewStoreRequest newStore;
	@Min(1) @Max(5)
	private Integer rating;
	@VisitedDateFormat
	private String visitedAt;
	private String imageUrl;
	@NotBlank
	@Size(min = 10, max = 300)
	private String description;

	public Feed toEntity(Store store, User user, Integer visitTimes) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
		LocalDate dateformat = LocalDate.parse(visitedAt, formatter);

		return Feed.builder()
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
