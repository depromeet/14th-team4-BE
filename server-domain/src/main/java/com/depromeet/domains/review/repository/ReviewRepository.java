package com.depromeet.domains.review.repository;

import com.depromeet.domains.review.entity.Review;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //리뷰 이미지가 존재하는 가장 최근 10개의 리뷰 조회
    List<Review> findTop10ByStoreOrderByCreatedAtDesc(Store store);

    // 자신이 특정 음식점에 몇번째 재방문인지 조회
    Long countByStoreAndUser(Store store, User user);

    // 특정 음식점에 전체 재방문 인원 조회
    @Query("SELECT COUNT(DISTINCT r.user) FROM Review r WHERE r.store = :store GROUP BY r.user HAVING COUNT(r) >= 2")
    Long countTotalRevisitedCount(@Param("store") Store store);

    // 맛집 최고의 단골의 방문 수
    @Query("SELECT MAX(r.visitCount) FROM (SELECT COUNT(r) as visitCount FROM Review r WHERE r.store = :store GROUP BY r.user) AS r")
    Long maxReviewCount(@Param("store") Store store);

    // 재방문 리뷰만 조회
    @Query("SELECT r FROM Review r WHERE r.store = :store AND r.user IN " +
            "(SELECT u FROM Review u WHERE u.store = :store GROUP BY u.user HAVING COUNT(u) >= 2) " +
            "ORDER BY r.createdAt DESC")
    List<Review> findRevisitedReviews(@Param("store") Store store);

    // 사진 리뷰만 조회
    List<Review> findByImageUrlIsNotNullOrderByCreatedAtDesc();


    List<Review> findByStore(Store store);
}
