package com.depromeet.store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.domain.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@GetMapping("/")
	public String root() {
		return "hello root";
	}

	@GetMapping("/stores")
	public ResponseEntity getStores() {
		return ResponseEntity.status(HttpStatus.OK).body(this.storeService.findAll());
	}
}