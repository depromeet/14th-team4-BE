package com.depromeet.domains.feed.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.depromeet.domains.feed.entity.Feed;

public interface FeedRepositoryCustom {
	boolean existsByStoreAndUser(Long storeId, Long userId);

	Slice<Feed> findByUserId(Long userId, Pageable pageable);
}
