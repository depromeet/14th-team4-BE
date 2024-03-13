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
		private String address;
		private Double longitude;
		private Double latitude;
		private Long totalFeedCnt;
		private Boolean isBookmarked;

		public static StoreLocationRange of(Long storeId, Long kakaoStoreId, String storeName,
			String address, Double longitude, Double latitude, Long totalFeedCnt
			, Boolean isBookmarked) {
			return StoreLocationRange.builder()
				.storeId(storeId)
				.kakaoStoreId(kakaoStoreId)
				.storeName(storeName)
				.address(address)
				.longitude(longitude)
				.latitude(latitude)
				.totalFeedCnt(totalFeedCnt)
				.isBookmarked(isBookmarked)
				.build();
		}
	}
}
