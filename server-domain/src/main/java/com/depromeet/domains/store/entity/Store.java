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

	@Column(nullable = false)
	private String storeName;

	@Embedded
	private StoreLocation storeLocation;

	private String storeAddress;

	private String thumbnailUrl;

	private Float totalRating;

	private Long totalReviewCount;

	private Long kakaoStoreId;

	private Long revisitedCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
}
