package com.depromeet.domains.follow.repository;

import com.depromeet.domains.follow.dto.FollowListResponse;
import com.depromeet.domains.follow.entity.Follow;
import com.depromeet.enums.FollowType;

import java.util.List;

public interface FollowRepositoryCustom {

    Long getFollowingCountBySenderId(Long senderId);
    Long getFollowerCountByReceiverId(Long receiverId);

    List<Follow> getFollowByEachId(Long senderId, Long receiverId);
	// 팔로우 목록 조회
	List<FollowListResponse> findFollowList(Long userId, Long targetUserId, FollowType followType);

}
