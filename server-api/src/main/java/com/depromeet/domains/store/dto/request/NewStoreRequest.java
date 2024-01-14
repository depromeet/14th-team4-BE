package com.depromeet.domains.store.dto.request;

import com.depromeet.domains.category.entity.Category;
import com.depromeet.domains.store.entity.Location;
import com.depromeet.domains.store.entity.Store;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NewStoreRequest {
	private String storeName;
	private Double latitude;
	private Double longitude;
	private Long categoryId;
	private String address;

	public Store toEntity(Category category) {

		return Store.builder()
			.storeName(this.storeName)
			.location(new Location(this.latitude, this.longitude))
			.address(this.address)
			.category(category)
			.totalRating(0.0F)
			.totalReviewCount(1L)
			.build();
	}
}
