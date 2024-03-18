package com.depromeet.domains.follow.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.depromeet.domains.follow.entity.QFollow.follow;


@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

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

    private BooleanExpression senderIdEq(Long senderId) {
        return follow.senderId.eq(senderId);
    }

    private BooleanExpression receiverIdEq(Long receiverId) {
        return follow.receiverId.eq(receiverId);
    }
}
