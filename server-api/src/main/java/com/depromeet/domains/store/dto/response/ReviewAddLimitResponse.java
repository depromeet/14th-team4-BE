package com.depromeet.domains.store.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewAddLimitResponse {
	private Boolean isAvailable;

	public static ReviewAddLimitResponse of(boolean isAvailable) {
		return ReviewAddLimitResponse.builder()
			.isAvailable(isAvailable)
			.build();
	}
}
