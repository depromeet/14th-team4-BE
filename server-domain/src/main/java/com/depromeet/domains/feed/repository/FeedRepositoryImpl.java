package com.depromeet.domains.feed.repository;

import java.util.List;

import com.depromeet.domains.feed.dto.response.FeedDetailResponse;
import com.depromeet.domains.feed.dto.response.FeedResponse;
import com.depromeet.domains.follow.entity.QFollow;
import com.depromeet.domains.store.dto.StoreFeedResponse;
import com.depromeet.domains.store.entity.QStore;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.depromeet.domains.feed.entity.Feed;
import com.depromeet.domains.feed.entity.QFeed;
import java.time.LocalDateTime;

import com.depromeet.domains.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.querydsl.jpa.JPAExpressions.select;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QFeed feed = QFeed.feed;
    QUser user = QUser.user;
    QFollow follow = QFollow.follow;
    QStore store = QStore.store;

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
    public List<StoreFeedResponse> findFeedByStoreId(Long storeId, Long userId, Long lastFeedId, Integer size) {
        List<StoreFeedResponse> results = jpaQueryFactory
                .select(Projections.fields(StoreFeedResponse.class,
                        feed.userId,
                        feed.feedId,
                        user.profileImageUrl,
                        user.nickname,
                        feed.rating,
                        feed.imageUrl,
                        feed.createdAt,
                        feed.description,
                        feed.userId.eq(userId)))
                .from(feed)
                .join(user).on(feed.userId.eq(user.userId))
                .where(ltFeedId(lastFeedId),
                        feed.storeId.eq(storeId))
                .orderBy(feed.createdAt.desc())
                .orderBy(feed.createdAt.desc())
                .limit(size+1)
                .fetch();

        return results;
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

    @Override
    public List<FeedResponse> findFeedAll(Long lastFeedId, Long userId, String type, Integer size) {
        BooleanExpression condition = getTypeCondition(userId, type);

        List<FeedResponse> results = jpaQueryFactory
                .select(Projections.constructor(FeedResponse.class,
                        user.userId,
                        user.profileImageUrl,
                        user.nickname,
                        store.storeId,
                        store.storeName,
                        store.kakaoCategoryName,
                        store.address,
                        feed.feedId,
                        feed.description,
                        feed.imageUrl,
                        feed.createdAt,
                        select(follow.receiverId.count().gt(0))
                                .from(follow)
                                .where(follow.senderId.eq(userId)
                                        .and(follow.receiverId.eq(feed.userId)))
                                .exists().as("isFollowed")))
                .from(feed)
                .join(store).on(feed.storeId.eq(store.storeId))
                .join(user).on(feed.userId.eq(user.userId))
                .where(condition,
                        ltFeedId(lastFeedId))
                .orderBy(feed.createdAt.desc())
                .limit(size+1)
                .fetch();

        return results;
    }

    @Override
    public FeedDetailResponse findFeedDetail(Long userId, Long feedId) {

        BooleanExpression isFollowedSubQuery = select(follow.receiverId.count().gt(0))
                .from(follow)
                .where(follow.senderId.eq(userId)
                        .and(follow.receiverId.eq(feed.userId)))
                .exists();

        FeedDetailResponse result = jpaQueryFactory
                .select(Projections.constructor(FeedDetailResponse.class,
                        user.userId,
                        user.profileImageUrl,
                        user.nickname,
                        store.storeId,
                        store.storeName,
                        store.kakaoCategoryName,
                        store.address,
                        feed.feedId,
                        feed.description,
                        feed.imageUrl,
                        feed.createdAt,
                        isFollowedSubQuery, // 서브쿼리 결과를 생성자 인자로 전달
                        feed.rating))
                .from(feed)
                .join(store).on(feed.storeId.eq(store.storeId))
                .join(user).on(feed.userId.eq(user.userId))
                .where(feed.feedId.eq(feedId))
                .fetchOne();
        return result;
    }

    // type에 따른 조건 생성
    private BooleanExpression getTypeCondition(Long userId, String type) {
        if ("FOLLOW".equals(type)) {
            return feed.userId.in(
                    JPAExpressions
                            .select(follow.receiverId)
                            .from(follow)
                            .where(follow.senderId.eq(userId))
            );
        } else { // "ALL" 타입이거나 다른 타입일 경우
            return null; // 모든 피드를 조회하기 위해 null을 반환
        }
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltFeedId(Long feedId) {
        if (feedId == null) {
            return null;
        }

        return feed.feedId.lt(feedId);
    }
}
