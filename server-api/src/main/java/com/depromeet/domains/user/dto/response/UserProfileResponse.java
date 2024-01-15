package com.depromeet.domains.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
	private String nickname;
	private String level;

	public static UserProfileResponse of(String nickname, String level) {
		return UserProfileResponse.builder()
			.nickname(nickname)
			.level(level)
			.build();
	}
}
