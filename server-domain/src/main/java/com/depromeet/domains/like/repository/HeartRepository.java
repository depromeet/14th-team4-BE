package com.depromeet.domains.like.repository;

import com.depromeet.domains.like.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long>, HeartRepositoryCustom {
}
