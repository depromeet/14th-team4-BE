package com.depromeet.domains.feed.repository;



import com.depromeet.domains.store.dto.StoreFeedResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import java.time.LocalDateTime;


import com.depromeet.domains.feed.entity.Feed;

public interface FeedRepositoryCustom {
	Long countStoreReviewByUserForDay(Long userId, Long storeId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<String> findTop10FeedImagesByCreatedAtDesc(Long storeId);
    Slice<StoreFeedResponse> findFeedByStoreId(Long storeId, Pageable pageable, Long userId);
	boolean existsByStoreAndUser(Long storeId, Long userId);

	Slice<Feed> findByUserId(Long userId, Pageable pageable);
}
