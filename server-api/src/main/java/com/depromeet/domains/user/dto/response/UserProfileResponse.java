package com.depromeet.domains.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
	private Long userId;
	private String nickname;
	private String level;

	public static UserProfileResponse of(Long userId, String nickname, String level) {
		return UserProfileResponse.builder()
			.userId(userId)
			.nickname(nickname)
			.level(level)
			.build();
	}
}
