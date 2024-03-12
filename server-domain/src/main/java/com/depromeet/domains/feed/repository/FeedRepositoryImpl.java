package com.depromeet.domains.feed.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.depromeet.domains.feed.entity.Feed;
import com.depromeet.domains.feed.entity.QFeed;
import com.depromeet.domains.feed.entity.QFeed;
import com.depromeet.domains.store.dto.response.StoreFeedResponse;
import java.time.LocalDateTime;

import com.depromeet.domains.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QFeed feed = QFeed.feed;


    @Override
    public Long countStoreReviewByUserForDay(Long userId, Long storeId, LocalDateTime startOfDay,
        LocalDateTime endOfDay) {
        return jpaQueryFactory.select(feed.count())
            .from(feed)
            .where(feed.userId.eq(userId)
                .and(feed.storeId.eq(storeId))
                .and(feed.createdAt.between(startOfDay, endOfDay)))
            .fetchOne();
    }
    @Override
    public List<String> findTop10FeedImagesByCreatedAtDesc(Long storeId) {
        List<String> imageUrls = jpaQueryFactory
                .select(feed.imageUrl)
                .from(feed)
                .where(feed.storeId.eq(storeId))
                .where(feed.deletedAt.isNull())
                .orderBy(feed.createdAt.desc())
                .limit(10)
                .fetch();
        return imageUrls;
    }

    @Override
    public Slice<StoreFeedResponse> findFeedByStoreId(Long storeId, Pageable pageable, Long userId) {
        QFeed feed = QFeed.feed;
        QUser user = QUser.user;

        List<StoreFeedResponse> results = jpaQueryFactory
                .select(Projections.fields(StoreFeedResponse.class,
                        feed.userId,
                        feed.feedId,
                        user.profileImageUrl,
                        user.nickName,
                        feed.rating,
                        feed.imageUrl,
                        feed.createdAt,
                        feed.description,
                        feed.userId.eq(userId)))
                .from(feed)
                .join(user).on(feed.userId.eq(user.userId))
                .where(ltFeedId((long) pageable.getPageNumber()),
                        feed.storeId.eq(storeId))
                .orderBy(feed.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltFeedId(Long feedId) {
        if (feedId == null) {
            return null;
        }

        return feed.feedId.lt(feedId);
    }

    // 무한 스크롤 방식 처리하는 메서드
    private Slice<StoreFeedResponse> checkLastPage(Pageable pageable, List<StoreFeedResponse> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

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
