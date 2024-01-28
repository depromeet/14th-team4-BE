package com.depromeet.domains.store.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreLocationRangeResponse {

	private List<StoreLocationRange> locationStoreList;

	public static StoreLocationRangeResponse of(List<StoreLocationRange> locationStoreList) {
		return StoreLocationRangeResponse.builder()
			.locationStoreList(locationStoreList)
			.build();
	}

	@Getter
	@Builder
	public static class StoreLocationRange {
		private Long storeId;
		private Long kakaoStoreId;
		private String storeName;
		private Long categoryId;
		private String categoryName;
		private String categoryType;
		private String address;
		private Double longitude;
		private Double latitude;
		private Long totalRevisitedCount;
		private Long totalReviewCount;
		private Boolean isBookMarked;

		public static StoreLocationRange of(Long storeId, Long kakaoStoreId, String storeName, Long categoryId,
			String categoryName, String categoryType, String address, Double longitude, Double latitude,
			Long totalRevisitedCount, Long totalReviewCount, Boolean isBookMarked) {
			return StoreLocationRange.builder()
				.storeId(storeId)
				.kakaoStoreId(kakaoStoreId)
				.storeName(storeName)
				.categoryId(categoryId)
				.categoryName(categoryName)
				.categoryType(categoryType)
				.address(address)
				.longitude(longitude)
				.latitude(latitude)
				.totalRevisitedCount(totalRevisitedCount)
				.totalReviewCount(totalReviewCount)
				.isBookMarked(isBookMarked)
				.build();
		}
	}
}
