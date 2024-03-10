package com.depromeet.domains.store.dto.request;

import com.depromeet.domains.feed.entity.Feed;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.user.entity.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	@NotNull
	private String imageUrl;
	@NotBlank
	@Size(min = 10, max = 300)
	private String description;

	public Feed toEntity(User user) {
		return Feed.builder()
			.storeId(storeId)
			.userId(user.getUserId())
			.rating(rating)
			.imageUrl(imageUrl)
			.description(description)
			.build();
	}

	public boolean hasStoreId() {
		return this.storeId != null;
	}
}
