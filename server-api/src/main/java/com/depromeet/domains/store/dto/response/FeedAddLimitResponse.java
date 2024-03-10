package com.depromeet.domains.store.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedAddLimitResponse {
	private Boolean isAvailable;

	public static FeedAddLimitResponse of(boolean isAvailable) {
		return FeedAddLimitResponse.builder()
			.isAvailable(isAvailable)
			.build();
	}
}
