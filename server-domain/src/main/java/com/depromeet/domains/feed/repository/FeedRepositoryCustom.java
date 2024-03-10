package com.depromeet.domains.feed.repository;

import java.time.LocalDateTime;

public interface FeedRepositoryCustom {
	Long countStoreReviewByUserForDay(Long userId, Long storeId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
