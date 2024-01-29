package com.depromeet.domains.store.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StorePreviewResponse {

	private Long storeId;
	private String categoryName;
	private String storeName;
	private String address;
	private Float totalRating;
	private Long totalReviewCount;
	private List<String> reviewImageUrls;
	private Long userId;
	private Long myRevisitedCount; // 자신이 재방문한 횟수(N번 방문)
	private Long totalRevisitedCount; // 전체 재방문 인원 수(00명이 재방문했어요)
	private Boolean isBookmarked;

	public static StorePreviewResponse of(Long storeId, String categoryName, String storeName, String address,
		Float totalRating, Long totalReviewCount, List<String> reviewImageUrls, Long userId, Long myRevisitedCount,
		Long totalRevisitedCount, Boolean isBookmarked) {
		return StorePreviewResponse.builder()
			.storeId(storeId)
			.categoryName(categoryName)
			.storeName(storeName)
			.address(address)
			.totalRating(totalRating)
			.totalReviewCount(totalReviewCount)
			.reviewImageUrls(reviewImageUrls)
			.userId(userId)
			.myRevisitedCount(myRevisitedCount)
			.totalRevisitedCount(totalRevisitedCount)
			.isBookmarked(isBookmarked)
			.build();
	}
}
