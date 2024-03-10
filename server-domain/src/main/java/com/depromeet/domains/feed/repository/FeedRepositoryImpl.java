package com.depromeet.domains.feed.repository;

import org.springframework.stereotype.Repository;

import com.depromeet.domains.feed.entity.QFeed;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByStoreAndUser(Long storeId, Long userId) {
        QFeed feed = QFeed.feed;
        Integer result = jpaQueryFactory
            .selectOne()
            .from(feed)
            .where(feed.storeId.eq(storeId)
                .and(feed.userId.eq(userId)))
            .fetchFirst();
        return result != null;
    }
}
