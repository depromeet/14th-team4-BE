package com.depromeet.domains.store.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreSharingSpotResponse {

	private List<StoreSharingSpot> locationStoreList; // 변수명은 StoreLocationRangeResponse 클래스와 동일(프런트에서 요청)

	public static StoreSharingSpotResponse of(List<StoreSharingSpot> storeSharingSpot) {
		return StoreSharingSpotResponse.builder()
			.locationStoreList(storeSharingSpot)
			.build();
	}

	@Getter
	@Builder
	public static class StoreSharingSpot {
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

		public static StoreSharingSpot of(Long storeId, Long kakaoStoreId, String storeName,
			Long categoryId,
			String categoryName, String categoryType, String address, Double longitude, Double latitude,
			Long totalRevisitedCount, Long totalReviewCount) {
			return StoreSharingSpot.builder()
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
				.build();
		}
	}
}
