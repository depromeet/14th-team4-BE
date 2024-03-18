package com.depromeet.domains.follow.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.depromeet.domains.follow.dto.FollowListResponse;
import com.depromeet.domains.follow.entity.QFollow;
import com.depromeet.domains.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{
	private final JPAQueryFactory jpaQueryFactory;
	QFollow follow = QFollow.follow;
	QUser user = QUser.user;
	QFollow subFollow = new QFollow("subFollow");

	@Override
	public List<FollowListResponse> findFollowersWithFollowStatus(Long userId, Long targetUserId) {
		List<FollowListResponse> followers = jpaQueryFactory
			.select(Projections.constructor(FollowListResponse.class,
				follow.senderId,
				user.nickName,
				user.profileImageUrl,
				new CaseBuilder()
					.when(JPAExpressions
						.selectOne()
						.from(subFollow)
						.where(subFollow.senderId.eq(userId)
							.and(subFollow.receiverId.eq(follow.senderId)))
						.exists())
					.then(true)
					.otherwise(false)
			))
			.from(follow)
			.join(user).on(follow.senderId.eq(user.userId))
			.where(follow.receiverId.eq(targetUserId)
				.and(follow.senderId.ne(userId))) // 자기 자신은 제외
			.fetch();

		return followers;
	}

	@Override
	public List<FollowListResponse> findFollowingsWithFollowStatus(Long userId, Long targetUserId) {
		List<FollowListResponse> followings = jpaQueryFactory
			.select(Projections.constructor(FollowListResponse.class,
				follow.receiverId,
				user.nickName,
				user.profileImageUrl,
				new CaseBuilder()
					.when(JPAExpressions
						.selectOne()
						.from(subFollow)
						.where(subFollow.senderId.eq(userId)
							.and(subFollow.receiverId.eq(follow.receiverId)))
						.exists())
					.then(true)
					.otherwise(false)
			))
			.from(follow)
			.join(user).on(follow.receiverId.eq(user.userId))
			.where(follow.senderId.eq(targetUserId)
				.and(follow.receiverId.ne(userId))) // 자기 자신은 제외
			.fetch();

		return followings;
	}
}
