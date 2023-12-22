package com.depromeet.domains.review.repository;

import com.depromeet.domains.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
