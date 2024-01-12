package com.depromeet.domains.user.dto.response;

import com.depromeet.domains.store.entity.StoreAddress;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserBookmarkResponse {
	private Long storeId; // 북마크한 가게 id
	private String storeName; // 북마크한 가게 이름
	private StoreAddress address; // 북마크한 가게 주소
	private Long totalRevisitedCount; // 북마크한 가게에 총 몇명이 재방문했는지
	private String categoryName; // 북마크한 가게의 카테고리
	private Boolean isVisited; // 북마크한 가게에 내가 방문했는지 안했는지

	public static UserBookmarkResponse of(Long storeId, String storeName, StoreAddress address, Long totalRevisitedCount,
		String categoryName, Boolean isVisited) {
		return UserBookmarkResponse.builder()
			.storeId(storeId)
			.storeName(storeName)
			.address(address)
			.totalRevisitedCount(totalRevisitedCount)
			.categoryName(categoryName)
			.isVisited(isVisited)
			.build();
	}
}
