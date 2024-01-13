package com.depromeet.domains.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NicknameRequest {
	@NotBlank
	@Size(min = 1, max = 8)
	private String nickname;

	public NicknameRequest(String nickname) {
		this.nickname = nickname;
	}
}
