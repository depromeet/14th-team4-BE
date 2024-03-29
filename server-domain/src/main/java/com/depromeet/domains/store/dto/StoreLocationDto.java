package com.depromeet.domains.store.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreLocationDto {

	private Long storeId;
	private String storeName;
	private Double latitude;
	private Double longitude;
	private boolean isBookmarked;

	public static StoreLocationDto of(Long storeId, String storeName, Double latitude, Double longitude,
		boolean isBookmarkedStore) {

		return StoreLocationDto.builder()
			.storeId(storeId)
			.storeName(storeName)
			.latitude(latitude)
			.longitude(longitude)
			.isBookmarked(isBookmarkedStore)
			.build();
	}
}
