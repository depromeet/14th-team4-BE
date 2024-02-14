package com.depromeet.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViewLevel {

	ONE(1, 1.0, Integer.MAX_VALUE),
	TWO(2, 1.0, Integer.MAX_VALUE),
	THREE(3, 1.0, Integer.MAX_VALUE),
	FOUR(4, 0.9, 20),
	FIVE(5, 0.8, 20),
	SIX(6, 0.7, 20),
	SEVEN(7, 0.6, 15),
	EIGHT(8, 0.5, 15),
	NINE(9, 0.4, 15),
	ETC(-1, 0.0, Integer.MAX_VALUE);

	private final int level;
	private final double ratio;
	private final int minCount;

	public static ViewLevel findByLevel(int level) {
		return Arrays.stream(ViewLevel.values())
			.filter(viewLevel -> viewLevel.getLevel() == level)
			.findFirst()
			.orElse(ViewLevel.ETC);
	}
}
