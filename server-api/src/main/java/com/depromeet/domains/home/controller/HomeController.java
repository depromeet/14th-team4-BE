package com.depromeet.domains.home.controller;
import com.depromeet.domains.user.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.annotation.AuthUser;

@RestController
public class HomeController {

	// test
	@GetMapping("/")
	public String root() {
		return "hello root(수정됨2)";
	}

	// test
	@GetMapping("/home")
	public String home() {
		return "hello home(수정됨2)";
	}

	// test
	@GetMapping("/jwt-test")
	public User getUserInfo(@AuthUser User user) {
		return user;
	}
}
