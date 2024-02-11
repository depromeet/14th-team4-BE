package com.depromeet.auth.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.depromeet.auth.dto.KakaoTokenResponse;

@FeignClient(name = "KakaoTokenClient", url = "https://kauth.kakao.com")
public interface KakaoTokenClient {
	@PostMapping("/oauth/token")
	KakaoTokenResponse getToken(@RequestParam("grant_type") String grantType,
		@RequestParam("client_id") String clientId,
		@RequestParam("redirect_uri") String redirectUri,
		@RequestParam("code") String code,
		@RequestParam("client_secret") String clientSecret);

}
