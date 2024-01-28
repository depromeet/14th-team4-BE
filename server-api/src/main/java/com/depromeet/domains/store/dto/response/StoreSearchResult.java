package com.depromeet.domains.store.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreSearchResult {

	private Long storeId;
	private Long kakaoStoreId;
	private String storeName;
	private String kakaoCategoryName;
	private String categoryType;
	private String address;
	private Integer distance;
	private Long totalRevisitedCount;
	private Double longitude;
	private Double latitude;

	public static StoreSearchResult of(Long storeId, Long kakaoStoreId, String storeName, String kakaoCategoryName
		, String categoryType, String address, Integer distance, Long totalRevisitedCount, Double longitude, Double latitude) {
		return StoreSearchResult.builder()
			.storeId(storeId)
			.kakaoStoreId(kakaoStoreId)
			.storeName(storeName)
			.kakaoCategoryName(kakaoCategoryName)
			.categoryType(categoryType)
			.address(address)
			.distance(distance)
			.totalRevisitedCount(totalRevisitedCount)
			.longitude(longitude)
			.latitude(latitude)
			.build();
	}

}
