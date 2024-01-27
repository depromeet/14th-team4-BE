package com.depromeet.domains.store.entity;

import com.depromeet.domains.category.entity.Category;
import com.depromeet.domains.common.entity.BaseTimeEntity;

import jakarta.persistence.*;
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

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "store_meta_id")
	private StoreMeta storeMeta;

	@Column(nullable = false)
	private String storeName;

	@Embedded
	private Location location;

	private String address;

	@Column(length = 5000)
	private String thumbnailUrl;

	private Long kakaoStoreId;

	public void setStoreMeta(StoreMeta storeMeta) {
		this.storeMeta = storeMeta;
	}

	public void setThumbnailUrl(String imageUrl) {
		this.thumbnailUrl = imageUrl;
	}
}
