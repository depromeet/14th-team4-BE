package com.depromeet.domains.store.dto.request;

import com.depromeet.domains.category.entity.Category;
import com.depromeet.domains.store.entity.Location;
import com.depromeet.domains.store.entity.Store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NewStoreRequest {
	@NotBlank
	private String storeName;
	@NotNull
	private Double latitude;
	@NotNull
	private Double longitude;
	@NotNull
	private String categoryType;
	@NotNull
	private Long kakaoStoreId;
	@NotNull
	private String kakaoCategoryName;
	@NotBlank
	private String address;

	public Store toEntity(Category category) {
		return Store.builder()
			.storeName(this.storeName)
			.location(new Location(this.latitude, this.longitude))
			.address(this.address)
			.category(category)
			.kakaoStoreId(this.kakaoStoreId)
			.build();
	}
}
