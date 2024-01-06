package com.depromeet.domains.store.controller;

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.store.dto.response.StorePreviewResponse;
import com.depromeet.domains.store.dto.response.StoreReportResponse;
import com.depromeet.domains.store.dto.response.StoreReviewResponse;
import com.depromeet.domains.store.service.StoreService;
import com.depromeet.domains.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@GetMapping("/stores/{storeId}")
	public CustomResponseEntity<StorePreviewResponse> getStore(@PathVariable Long storeId, @AuthUser User user) {
		return CustomResponseEntity.success(storeService.getStore(storeId, user));
	}

	@GetMapping("/stores/{storeId}/reports")
	public CustomResponseEntity<StoreReportResponse> getStoreReport(@PathVariable Long storeId) {
		return CustomResponseEntity.success(storeService.getStoreReport(storeId));
	}

	@GetMapping("/stores/{storeId}/logs")
	public CustomResponseEntity<Slice<StoreReviewResponse>> getStoreReview(@PathVariable Long storeId, @RequestParam("type") String type, Pageable pageable) {
		return CustomResponseEntity.success(storeService.getStoreReview(storeId, type, pageable));
	}


}
