package com.depromeet.auth.apple;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class AppleProperties {

	@Value("${social-login.provider.apple.client-id}")
	private String clientId;

	@Value("${social-login.provider.apple.grant-type}")
	private String grantType;

	@Value("${social-login.provider.apple.key-id}")
	private String keyId;

	@Value("${social-login.provider.apple.team-id}")
	private String teamId;

	@Value("${social-login.provider.apple.audience}")
	private String audience;

	@Value("${social-login.provider.apple.private-key}")
	private String privateKey;

}
