package com.depromeet.domains.store.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreSharingSpotResponse {

	private String userNickName;
	private List<StoreSharingSpot> locationStoreList; // 변수명은 StoreLocationRangeResponse 클래스와 동일(프런트에서 요청)

	public static StoreSharingSpotResponse of(String userNickName, List<StoreSharingSpot> storeSharingSpot) {
		return StoreSharingSpotResponse.builder()
			.userNickName(userNickName)
			.locationStoreList(storeSharingSpot)
			.build();
	}

	@Getter
	@Builder
	public static class StoreSharingSpot {
		private Long storeId;
		private Long kakaoStoreId;
		private String storeName;
		private String address;
		private Double longitude;
		private Double latitude;
		private Long totalFeedCnt;

		public static StoreSharingSpot of(Long storeId, Long kakaoStoreId, String storeName,
			String address, Double longitude, Double latitude, Long totalFeedCnt) {
			return StoreSharingSpot.builder()
				.storeId(storeId)
				.kakaoStoreId(kakaoStoreId)
				.storeName(storeName)
				.address(address)
				.longitude(longitude)
				.latitude(latitude)
				.totalFeedCnt(totalFeedCnt)
				.build();
		}
	}
}
