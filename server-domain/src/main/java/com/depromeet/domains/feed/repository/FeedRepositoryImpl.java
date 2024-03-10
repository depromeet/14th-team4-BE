package com.depromeet.domains.feed.repository;

import java.time.LocalDateTime;

import com.depromeet.domains.feed.entity.QFeed;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long countStoreReviewByUserForDay(Long userId, Long storeId, LocalDateTime startOfDay,
        LocalDateTime endOfDay) {
        QFeed feed = QFeed.feed;
        return jpaQueryFactory.select(feed.count())
            .from(feed)
            .where(feed.userId.eq(userId)
                .and(feed.storeId.eq(storeId))
                .and(feed.createdAt.between(startOfDay, endOfDay)))
            .fetchOne();
    }
}
