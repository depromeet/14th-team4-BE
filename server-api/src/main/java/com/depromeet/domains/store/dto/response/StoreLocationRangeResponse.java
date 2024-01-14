package com.depromeet.domains.store.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class StoreLocationRangeResponse {

	private List<LocationRangeResponse> response;

	public static StoreLocationRangeResponse of(List<LocationRangeResponse> list) {
		return StoreLocationRangeResponse.builder()
			.response(list)
			.build();
	}

	@Getter
	@Builder
	public static class LocationRangeResponse {
		private Long storeId;
		private String storeName;
		private Double longitude;
		private Double latitude;
		private boolean isBookMark;
		private int totalRevisitedCount;

		public static LocationRangeResponse of(Long storeId, String storeName, Double longitude, Double latitude,
			boolean isBookMark, int totalRevisitedCount) {
			return LocationRangeResponse.builder()
				.storeId(storeId)
				.storeName(storeName)
				.longitude(longitude)
				.latitude(latitude)
				.isBookMark(isBookMark)
				.totalRevisitedCount(totalRevisitedCount)
				.build();
		}
	}
}
