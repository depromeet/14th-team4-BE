package com.depromeet.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SocialType implements EnumType {
	APPLE("apple"), KAKAO("kakao");

	private final String description;

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public static SocialType fromString(String provider) {
		for (SocialType type : SocialType.values()) {
			if (type.getName().equalsIgnoreCase(provider)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unsupported provider: " + provider);
	}
}
