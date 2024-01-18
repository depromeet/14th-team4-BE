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

    /*
    최다 방문자가 겹치는 경우
     */
    // 3번 이상 재방문 한 사람이 리뷰 삭제시
    public void deletedReviewFromVisitedThreeOrMoreIfMostVisitorDuplicate(Integer rating) {
        this.totalRating = (this.totalRating * this.totalReviewCount - rating) / this.totalReviewCount - 1;
        this.totalReviewCount--;
    }

    // 2번 이하 재방문 한 사람이 리뷰 삭제시
    public void deletedReviewFromVisitedTwoOrLessIfMostVisitorDuplicate(Integer rating) {
        this.totalRating = (this.totalRating * this.totalReviewCount - rating) / this.totalReviewCount - 1;
        this.totalReviewCount--;
        this.totalRevisitedCount--;
    }

    /*
    최다 방문자가 나 혼자인 경우
     */
    // 3번 이상 재방문 한 사람이 리뷰 삭제시
    public void deleteReviewFromVisitedThreeOrMoreIfMostVisitorMe(Integer rating) {
        this.totalRating = (this.totalRating * this.totalReviewCount - rating) / this.totalReviewCount - 1;
        this.totalReviewCount--;
        this.mostVisitedCount--;
    }

    // 2번 이하 재방문 한 사람이 리뷰 삭제시
    public void deletedReviewFromVisitedTwoOrLessIfMostVisitorMe(Integer rating) {
        this.totalRating = (this.totalRating * this.totalReviewCount - rating) / this.totalReviewCount - 1;
        this.totalReviewCount--;
        this.totalRevisitedCount--;
        this.mostVisitedCount--;
    }

    /*
    최다 방문자가 아닌 경우
     */
    // 3번 이상 재방문 한 사람이 리뷰 삭제시
    public void deleteReviewFromVisitedThreeOrMore(Integer rating) {
        this.totalRating = (this.totalRating * this.totalReviewCount - rating) / this.totalReviewCount - 1;
        this.totalReviewCount--;
    }

    // 2번 이하 재방문 한 사람이 리뷰 삭제시
    public void deleteReviewFromVisitedTwoOrLess(Integer rating) {
        this.totalRating = (this.totalRating * this.totalReviewCount - rating) / this.totalReviewCount - 1;
        this.totalReviewCount--;
        this.totalRevisitedCount--;
    }
}
