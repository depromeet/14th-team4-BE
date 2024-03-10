package com.depromeet.domains.feed.repository;

public interface FeedRepositoryCustom {
	boolean existsByStoreAndUser(Long storeId, Long userId);

}
