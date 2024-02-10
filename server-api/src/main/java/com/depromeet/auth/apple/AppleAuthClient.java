package com.depromeet.auth.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.depromeet.auth.dto.ApplePublicKeyResponse;
import com.depromeet.auth.dto.AppleToken;

@Component
@FeignClient(
	name = "appleClient",
	url = "${social-login.provider.apple.audience}",
	configuration = AppleFeignClientConfiguration.class
)
public interface AppleAuthClient {

	/**
	 *  identityToken 서명 검증용 publicKey를 apple에 요청
	 *  public key는 여러개
	 */
	@GetMapping("/auth/keys")
	ApplePublicKeyResponse getAppleAuthPublicKey();

	@PostMapping(value = "/auth/token", consumes = "application/x-www-form-urlencoded")
	AppleToken.Response generateOrValidateToken(AppleToken.Request request);

}

