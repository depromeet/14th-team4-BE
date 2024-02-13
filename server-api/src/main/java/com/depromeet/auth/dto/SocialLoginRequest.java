package com.depromeet.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialLoginRequest {
	@NotEmpty
	private String provider;
	@NotEmpty
	private String code;
	private String redirect_uri;
}
