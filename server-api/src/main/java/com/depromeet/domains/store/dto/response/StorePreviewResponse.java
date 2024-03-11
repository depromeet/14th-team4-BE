package com.depromeet.domains.store.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StorePreviewResponse {

	private Long storeId;
	private String kakaoCategoryName;
	private String storeName;
	private String address;
	private Float totalRating;
	private Long totalFeedCnt;
	private List<String> feedImageUrls;
	private Long userId;
	private Boolean isBookmarked;

	public static StorePreviewResponse of(Long storeId, String kakaoCategoryName, String storeName, String address,
		Float totalRating, Long totalFeedCnt, List<String> feedImageUrls, Long userId, Boolean isBookmarked) {
		return StorePreviewResponse.builder()
			.storeId(storeId)
			.kakaoCategoryName(kakaoCategoryName)
			.storeName(storeName)
			.address(address)
			.totalRating(totalRating)
			.totalFeedCnt(totalFeedCnt)
			.feedImageUrls(feedImageUrls)
			.userId(userId)
			.isBookmarked(isBookmarked)
			.build();
	}
}
