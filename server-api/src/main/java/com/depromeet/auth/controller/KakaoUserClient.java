package com.depromeet.auth.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.depromeet.auth.dto.KakaoUserInfo;

@FeignClient(name = "kakaoUserClient", url = "https://kapi.kakao.com")
public interface KakaoUserClient {
	@GetMapping("/v2/user/me")
	KakaoUserInfo getUserInfo(@RequestHeader("Authorization") String token);
}

