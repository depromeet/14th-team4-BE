package com.depromeet.domains.follow.repository;

import com.depromeet.domains.follow.dto.FollowListResponse;
import com.depromeet.domains.follow.entity.Follow;
import com.depromeet.domains.follow.entity.QFollow;
import com.depromeet.domains.user.entity.QUser;
import com.depromeet.enums.FollowType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    
    QFollow follow = QFollow.follow;
    QUser user = QUser.user;
    QFollow subFollow = new QFollow("subFollow");

    @Override
    public Long getFollowingCountBySenderId(Long senderId) {
        return jpaQueryFactory
                .select(follow.count())
                .from(follow)
                .where(
                        senderIdEq(senderId)
                )
                .fetchOne();
    }

    @Override
    public Long getFollowerCountByReceiverId(Long receiverId) {
        return jpaQueryFactory
                .select(follow.count())
                .from(follow)
                .where(
                        receiverIdEq(receiverId)
                )
                .fetchOne();
    }

    @Override
    public List<Follow> getFollowByEachId(Long senderId, Long receiverId) {
        return jpaQueryFactory
                .select(follow)
                .from(follow)
                .where(
                        senderIdEq(senderId),
                        receiverIdEq(receiverId)
                )
                .fetch();
    }

    private BooleanExpression senderIdEq(Long senderId) {
        return follow.senderId.eq(senderId);
    }

    private BooleanExpression receiverIdEq(Long receiverId) {
        return follow.receiverId.eq(receiverId);
    }
    
    @Override
    public List<FollowListResponse> findFollowList(Long currentUserId, Long targetUserId, FollowType followType) {
        BooleanExpression condition = followType == FollowType.FOLLOWER
                ? follow.receiverId.eq(targetUserId).and(follow.senderId.ne(currentUserId))
                : follow.senderId.eq(targetUserId).and(follow.receiverId.ne(currentUserId));

        BooleanExpression subCondition = followType == FollowType.FOLLOWER
                ? subFollow.senderId.eq(currentUserId).and(subFollow.receiverId.eq(follow.senderId))
                : subFollow.senderId.eq(currentUserId).and(subFollow.receiverId.eq(follow.receiverId));

        NumberPath<Long> relatedUserId = followType == FollowType.FOLLOWER ? follow.senderId : follow.receiverId;

        BooleanExpression joinCondition = followType == FollowType.FOLLOWER
                ? follow.senderId.eq(user.userId)
                : follow.receiverId.eq(user.userId);

        List<FollowListResponse> followListResponses = jpaQueryFactory
                .select(Projections.constructor(FollowListResponse.class,
                        relatedUserId,
                        user.nickName,
                        user.profileImageUrl,
                        new CaseBuilder()
                                .when(JPAExpressions.selectOne()
                                        .from(subFollow)
                                        .where(subCondition)
                                        .exists())
                                .then(true)
                                .otherwise(false)
                ))
                .from(follow)
                .join(user).on(joinCondition)
                .where(condition)
                .fetch();

        return followListResponses;
    }
}
