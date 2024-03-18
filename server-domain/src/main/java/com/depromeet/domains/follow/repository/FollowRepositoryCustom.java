package com.depromeet.domains.follow.repository;

public interface FollowRepositoryCustom {

    Long getFollowingCountBySenderId(Long senderId);
    Long getFollowerCountByReceiverId(Long receiverId);
}
