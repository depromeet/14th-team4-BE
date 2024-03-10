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

	private float totalRating = 0.0f;

	private String kakaoCategoryName;

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
}
