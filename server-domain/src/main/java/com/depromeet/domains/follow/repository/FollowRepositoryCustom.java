package com.depromeet.domains.follow.repository;

import java.util.List;

import com.depromeet.domains.follow.dto.FollowListResponse;

public interface FollowRepositoryCustom {
	// 팔로우 목록 조회
	List<FollowListResponse> findFollowersWithFollowStatus(Long userId, Long targetUserId);
	List<FollowListResponse> findFollowingsWithFollowStatus(Long userId, Long targetUserId);
}
