package com.depromeet.domains.heart.repository;

import com.depromeet.domains.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {
}
