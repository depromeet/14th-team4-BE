package com.depromeet.domains.follow.repository;

import com.depromeet.domains.follow.entity.Follow;

import java.util.List;

import java.util.List;

import com.depromeet.domains.follow.dto.FollowListResponse;
import com.depromeet.enums.FollowType;

public interface FollowRepositoryCustom {

    Long getFollowingCountBySenderId(Long senderId);
    Long getFollowerCountByReceiverId(Long receiverId);

    List<Follow> getFollowByEachId(Long senderId, Long receiverId);
	// 팔로우 목록 조회
	List<FollowListResponse> findFollowList(Long userId, Long targetUserId, FollowType followType);

}
