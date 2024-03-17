package com.depromeet.domains.store.entity;

import com.depromeet.domains.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Store")
public class Store extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long storeId;

	@Column(nullable = false)
	private String storeName;

	@Embedded
	private Location location;

	private String address;

	@Column(length = 5000)
	private String thumbnailUrl;

	private Long kakaoStoreId;

	@Builder.Default
	private float totalRating = 0.0f;

	private String kakaoCategoryName;

	@Builder.Default
	private Long totalFeedCnt = 0L;

	public void setThumbnailUrl(String imageUrl) {
		this.thumbnailUrl = imageUrl;
	}

	public void increaseTotalRating(float totalRating) {
		this.totalRating += totalRating;
	}

	public void decreaseTotalRating(float totalRating) {
		this.totalRating -= totalRating;
	}

	public void increaseTotalFeedCnt() {
		this.totalFeedCnt++;
	}

	public void decreaseTotalFeedCnt() {
		this.totalFeedCnt--;
	}

	public void updateTotalRating(Integer newRating) {
		this.totalRating = (this.totalRating * this.totalRating + newRating) / (this.totalFeedCnt + 1); // 새 평균 평점 계산
		this.totalRating = Math.round(this.totalRating * 10.0f) / 10.0f; // 반올림하여 저장
	}

	public void updateThumbnailUrl(String imageUrl) {
		this.thumbnailUrl = imageUrl;
	}
}
