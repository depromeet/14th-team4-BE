package com.depromeet.domains.store.entity;

import com.depromeet.domains.category.entity.Category;
import com.depromeet.domains.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Store")
public class Store extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long storeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(nullable = false)
	private String storeName;

	@Embedded
	private Location location;

	private String address;

	private String thumbnailUrl;

	private Float totalRating;

	private Long totalReviewCount;

	private Long kakaoStoreId;

	private Long myRevisitedCount;

	public void updateStoreSummary(Integer rating) {
		float totalRatingSum = this.totalRating * this.totalReviewCount;
		totalRatingSum += rating;
		this.totalReviewCount = this.totalReviewCount + 1;
		this.totalRating = totalRatingSum / this.totalReviewCount;
	}

	// 기존에 썸네일 없었던 경우에만 업데이트
	public void updateThumnailUrl(String thumbnailUrl) {
		if (this.thumbnailUrl == null) {
			this.thumbnailUrl = thumbnailUrl;
		}
	}
}
