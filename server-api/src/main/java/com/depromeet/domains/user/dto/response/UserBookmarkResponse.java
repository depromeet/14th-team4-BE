package com.depromeet.domains.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserBookmarkResponse {
	private Long bookmarkId;
	private Long storeId; // 북마크한 가게 id
	private String storeName; // 북마크한 가게 이름
	private String address; // 북마크한 가게 주소
	private String kakaoCategoryName; // 북마크한 가게의 카테고리
	private Boolean isVisited; // 북마크한 가게에 내가 방문했는지 안했는지

	public static UserBookmarkResponse of(Long bookmarkId, Long storeId, String storeName, String address,
		String kakaoCategoryName, Boolean isVisited) {
		return UserBookmarkResponse.builder()
			.bookmarkId(bookmarkId)
			.storeId(storeId)
			.storeName(storeName)
			.address(address)
			.kakaoCategoryName(kakaoCategoryName)
			.isVisited(isVisited)
			.build();
	}
}
