package com.depromeet.domains.feed.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.depromeet.domains.feed.entity.Feed;
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

    @Override
    public Slice<Feed> findByUserId(Long userId, Pageable pageable) {
        QFeed feed = QFeed.feed;
        List<Feed> results = jpaQueryFactory.selectFrom(feed)
            .where(feed.userId.eq(userId))
            .orderBy(feed.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1) // 다음 페이지가 있는지 확인하기 위해 1개 더 가져옴
            .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        List<Feed> content = hasNext ? results.subList(0, pageable.getPageSize()) : results;

        return new SliceImpl<>(content, pageable, hasNext);
    }
}
