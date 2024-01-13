package com.depromeet.domains.store.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewAddResponse {
	private Long reviewId;
	private Long storeId;

	public static ReviewAddResponse of(Long reviewId, Long storeId) {
		return ReviewAddResponse.builder()
			.reviewId(reviewId)
			.storeId(storeId)
			.build();
	}
}
