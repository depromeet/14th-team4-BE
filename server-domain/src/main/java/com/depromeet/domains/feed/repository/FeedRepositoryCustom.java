package com.depromeet.domains.feed.repository;



import com.depromeet.domains.feed.dto.response.FeedDetailResponse;
import com.depromeet.domains.feed.dto.response.FeedResponse;
import com.depromeet.domains.store.dto.StoreFeedResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import java.time.LocalDateTime;


import com.depromeet.domains.feed.entity.Feed;

public interface FeedRepositoryCustom {
	Long countStoreReviewByUserForDay(Long userId, Long storeId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<String> findTop10FeedImagesByCreatedAtDesc(Long storeId);
    List<StoreFeedResponse> findFeedByStoreId(Long storeId, Long userId, Long lastFeedId, Integer size);
	boolean existsByStoreAndUser(Long storeId, Long userId);

	Slice<Feed> findByUserId(Long userId, Pageable pageable);

	// type 별 피드 조회(ALL, FOLLOW)
	List<FeedResponse> findFeedAll(Long lastFeedId, Long userId, String type, Integer size);

	FeedDetailResponse findFeedDetail(Long userId, Long feedId);
}
