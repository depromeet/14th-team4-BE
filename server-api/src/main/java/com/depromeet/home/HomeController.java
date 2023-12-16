package com.depromeet.home;


import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.depromeet.entity.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.user.entity.User;

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
	public User getUserInfo(@AuthenticationPrincipal User user) {
		return user;
	}

	// rest docs test
	@GetMapping("/hello/{name}")
	public ResponseEntity<Person> hello(@PathVariable String name) {
		return ResponseEntity.ok()
				.body(new Person(name, "안녕하세요!테스트 2번째"));
	}
}
