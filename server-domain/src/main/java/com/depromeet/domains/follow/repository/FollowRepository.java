package com.depromeet.domains.follow.repository;

import com.depromeet.domains.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
