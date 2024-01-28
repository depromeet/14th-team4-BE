package com.depromeet.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CategoryType implements EnumType {

	KOREAN("KOREAN", "한식"),
	CHINESE("CHINESE", "중식"),
	JAPANESE("JAPANESE", "일식"),
	WESTERN("WESTERN", "양식"),
	CAFE("CAFE", "카페"),
	BARS("BARS", "술집"),
	SCHOOLFOOD("SCHOOLFOOD", "분식"),
	ETC("ETC", "기타");

	private final String type;
	private final String description;

	public static CategoryType findByType(String categoryType) {
		return Arrays.asList(CategoryType.values())
			.stream()
			.filter(t -> categoryType.equalsIgnoreCase(t.getType()))
			.findFirst()
			.orElse(CategoryType.ETC);
	}

	public static Optional<CategoryType> fromDescription(String description) {
		return Arrays.stream(CategoryType.values())
			.filter(type -> type.getDescription().equals(description))
			.findFirst();
	}

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public String getType() {
		return this.type;
	}
}
