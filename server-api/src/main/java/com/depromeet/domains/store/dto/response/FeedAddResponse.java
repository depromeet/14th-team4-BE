package com.depromeet.domains.store.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedAddResponse {
	private Long feedId;
	private Long storeId;

	public static FeedAddResponse of(Long feedId, Long storeId) {
		return FeedAddResponse.builder()
			.feedId(feedId)
			.storeId(storeId)
			.build();
	}
}
