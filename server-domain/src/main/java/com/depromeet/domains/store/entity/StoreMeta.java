package com.depromeet.domains.store.entity;

import com.depromeet.domains.common.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreMeta extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeMetaId;

    // 전체 재방문 인원(2번이상 방문)
    private Long totalRevisitedCount;

    // 전체 리뷰 개수
    private Long totalReviewCount;

    // 최다 재방문자의 재방문 횟수(이 맛집 최고 단골은 00번 방문)
    private Long mostVisitedCount;

    // 음식점 별점 평균
    private Float totalRating;

    public void updateTotalRating(Integer newRating) {
        float newTotalRatingSum = this.totalRating * this.totalReviewCount + newRating;
        this.totalReviewCount++; // 리뷰 개수 증가
        this.totalRating = newTotalRatingSum / this.totalReviewCount; // 새 평균 평점 계산
        this.totalRating = Math.round(this.totalRating * 10.0f) / 10.0f; // 반올림하여 저장
    }

    public void incrementTotalRevisitedCount() {
        this.totalRevisitedCount++;
    }

    public void updateMostRevisitedCount(Long userReviewCount) {
        if (userReviewCount > this.mostVisitedCount) {
            this.mostVisitedCount = userReviewCount;
        }
    }
}
