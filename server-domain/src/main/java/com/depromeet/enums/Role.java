package com.depromeet.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role implements EnumType {

	GUEST("ROLE_GUEST"), USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

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
