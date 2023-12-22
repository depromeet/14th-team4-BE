package com.depromeet.controller.store;

import com.depromeet.service.store.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@GetMapping("/stores")
	public ResponseEntity getStores() {
		return ResponseEntity.status(HttpStatus.OK).body(this.storeService.findAll());
	}
}
