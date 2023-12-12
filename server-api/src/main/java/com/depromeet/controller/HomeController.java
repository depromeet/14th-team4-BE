package com.depromeet.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.user.entity.User;

@RestController
public class HomeController {

	// test
	@GetMapping("/")
	public String root() {
		return "hello root";
	}

	// test
	@GetMapping("/home")
	public String home() {
		return "hello home";
	}

	// test
	@GetMapping("/jwt-test")
	public User getUserInfo(@AuthenticationPrincipal User user) {
		return user;
	}
}
