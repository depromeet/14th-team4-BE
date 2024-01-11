package com.depromeet.domains.store.entity;

import com.depromeet.domains.category.entity.Category;
import com.depromeet.domains.common.entity.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

	@Column(nullable = false)
	private Double latitude;

	@Column(nullable = false)
	private Double longitude;

	private String thumbnailUrl;

	private String jibunAddress;

	private String roadAddress;

	private String addressDetail;

	private Float totalRating;

	private Long totalReviewCount;

	public void updateStoreSummary(Float rating) {
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
