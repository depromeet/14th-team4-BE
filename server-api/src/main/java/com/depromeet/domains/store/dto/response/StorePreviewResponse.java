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
	private Long reviewCount;
	private List<String> reviewImageUrls;
	private Long userId;
	private Long revisitedCount; // 자신이 재방문한 횟수(N번 방문)
	private Long totalRevisitedCount; // 전체 재방문 인원 수(00명이 재방문했어요)

	public static StorePreviewResponse of(Long storeId, String categoryName, String storeName, String address,
		Float totalRating, Long reviewCount, List<String> reviewImageUrls, Long userId, Long revisitedCount,
		Long totalRevisitedCount) {
		return StorePreviewResponse.builder()
			.storeId(storeId)
			.categoryName(categoryName)
			.storeName(storeName)
			.address(address)
			.totalRating(totalRating)
			.reviewCount(reviewCount)
			.reviewImageUrls(reviewImageUrls)
			.userId(userId)
			.revisitedCount(revisitedCount)
			.totalRevisitedCount(totalRevisitedCount)
			.build();
	}
}
