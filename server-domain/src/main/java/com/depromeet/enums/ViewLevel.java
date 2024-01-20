package com.depromeet.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViewLevel {

	ONE(1, 1.0),
	TWO(2, 1.0),
	THREE(3, 1.0),
	FOUR(4, 0.9),
	FIVE(5, 0.8),
	SIX(6, 0.7),
	SEVEN(7, 0.6),
	EIGHT(8, 0.5),
	NINE(9, 0.4),
	ETC(-1, 0.0);

	private final int level;
	private final double ratio;

	public static ViewLevel findByLevel(int level) {
		return Arrays.stream(ViewLevel.values())
			.filter(viewLevel -> viewLevel.getLevel() == level)
			.findFirst()
			.orElse(ViewLevel.ETC);
	}
}
