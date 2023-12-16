package com.depromeet.home;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
