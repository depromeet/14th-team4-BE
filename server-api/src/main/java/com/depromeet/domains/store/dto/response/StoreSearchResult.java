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
	private Long revisitedCount;
	private Double latitude;
	private Double longitude;

	public static StoreSearchResult of(Long storeId, String storeName, String categoryName
		, String address, Integer distance, Long revisitedCount, Double latitude, Double longitude) {
		return StoreSearchResult.builder()
			.storeId(storeId)
			.storeName(storeName)
			.categoryName(categoryName)
			.address(address)
			.distance(distance)
			.revisitedCount(revisitedCount)
			.latitude(latitude)
			.longitude(longitude)
			.build();
	}

}
