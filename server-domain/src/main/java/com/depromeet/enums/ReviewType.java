package com.depromeet.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReviewType implements EnumType {
	REVISITED("재방문 리뷰"), PHOTO("사진 리뷰");
	private final String description;

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public String getDescription() {
		return this.description;
	}
}
