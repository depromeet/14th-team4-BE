package com.depromeet.domains.follow.repository;

import java.util.List;

import com.depromeet.domains.follow.dto.FollowListResponse;
import com.depromeet.enums.FollowType;

public interface FollowRepositoryCustom {
	// 팔로우 목록 조회
	List<FollowListResponse> findFollowList(Long userId, Long targetUserId, FollowType followType);

}
