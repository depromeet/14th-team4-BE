package com.depromeet.domains.follow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.domains.follow.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom{
	Optional<Follow> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
