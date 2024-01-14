package com.depromeet.domains.store.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreSearchResult {

	private Long storeId;
	private String storeName;
	private String categoryName;
	private String address;
	private Integer distance;
	private Long totalRevisitedCount;
	private Double latitude;
	private Double longitude;

	public static StoreSearchResult of(Long storeId, String storeName, String categoryName
		, String address, Integer distance, Long totalRevisitedCount, Double latitude, Double longitude) {
		return StoreSearchResult.builder()
			.storeId(storeId)
			.storeName(storeName)
			.categoryName(categoryName)
			.address(address)
			.distance(distance)
			.totalRevisitedCount(totalRevisitedCount)
			.latitude(latitude)
			.longitude(longitude)
			.build();
	}

}
