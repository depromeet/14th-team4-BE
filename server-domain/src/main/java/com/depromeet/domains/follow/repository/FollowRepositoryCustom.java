package com.depromeet.domains.follow.repository;

import com.depromeet.domains.follow.entity.Follow;

import java.util.List;

public interface FollowRepositoryCustom {

    Long getFollowingCountBySenderId(Long senderId);
    Long getFollowerCountByReceiverId(Long receiverId);

    List<Follow> getFollowByEachId(Long senderId, Long receiverId);
}
