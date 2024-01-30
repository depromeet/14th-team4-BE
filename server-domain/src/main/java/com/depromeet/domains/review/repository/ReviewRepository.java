package com.depromeet.domains.review.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.depromeet.domains.review.entity.Review;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.user.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //리뷰 이미지가 존재하는 가장 최근 10개의 리뷰 조회
    List<Review> findTop10ByStoreOrderByVisitedAtDesc(Store store);

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
            "(SELECT u.user FROM Review u WHERE u.store = :store GROUP BY u.user HAVING COUNT(u) >= 2) " +
            "ORDER BY r.visitedAt DESC")
    Slice<Review> findRevisitedReviews(@Param("store") Store store, Pageable pageable);

    // 사진 리뷰만 조회
    Slice<Review> findByStoreAndImageUrlIsNotNullOrderByVisitedAtDesc(Store store, Pageable pageable);

    boolean existsByStoreAndUser(Store store, User user);

    Slice<Review> findByUser(User user, Pageable pageable);
    List<Review> findByStore(Store store);

    @Query(value = "SELECT COUNT(*) FROM (SELECT COUNT(reviewId) AS review_count FROM Review WHERE store_id = :storeId GROUP BY user_id) AS subquery WHERE review_count = :count", nativeQuery = true)
    Long countByStoreAndReviewCount(@Param("storeId") Long storeId, @Param("count") Long count);

    Slice<Review> findByStore(Store store, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.store = :store AND r.user = :user AND r.createdAt BETWEEN :startOfDay AND :endOfDay")
    int countStoreReviewByUserForDay(@Param("user") User user, @Param("store") Store store,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay);

}
