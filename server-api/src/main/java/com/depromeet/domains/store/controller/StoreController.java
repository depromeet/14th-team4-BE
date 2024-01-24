package com.depromeet.domains.store.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.store.dto.request.ReviewRequest;
import com.depromeet.domains.store.dto.response.ReviewAddLimitResponse;
import com.depromeet.domains.store.dto.response.ReviewAddResponse;
import com.depromeet.domains.store.dto.response.StoreLocationRangeResponse;
import com.depromeet.domains.store.dto.response.StorePreviewResponse;
import com.depromeet.domains.store.dto.response.StoreReportResponse;
import com.depromeet.domains.store.dto.response.StoreReviewResponse;
import com.depromeet.domains.store.service.StoreService;
import com.depromeet.domains.user.entity.User;
import com.depromeet.enums.CategoryType;
import com.depromeet.enums.ReviewType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@GetMapping("/stores/location-range")
	public CustomResponseEntity<StoreLocationRangeResponse> getLocationRangeStores(
		@RequestParam(value = "latitude1") Double latitude1,
		@RequestParam(value = "longitude1") Double longitude1,
		@RequestParam(value = "latitude2") Double latitude2,
		@RequestParam(value = "longitude2") Double longitude2,
		@RequestParam(value = "level") Integer level,
		@RequestParam(value = "type") Optional<CategoryType> categoryType,
		@AuthUser User user) {
		return CustomResponseEntity.success(
			storeService.getRangeStores(latitude1, longitude1, latitude2, longitude2, level, categoryType,
				user));
	}

	@GetMapping("/stores/{storeId}")
	public CustomResponseEntity<StorePreviewResponse> getStore(@PathVariable Long storeId, @AuthUser User user) {
		return CustomResponseEntity.success(storeService.getStore(storeId, user));
	}

	@GetMapping("/stores/{storeId}/reports")
	public CustomResponseEntity<StoreReportResponse> getStoreReport(@PathVariable Long storeId) {
		return CustomResponseEntity.success(storeService.getStoreReport(storeId));
	}

	@GetMapping("/stores/{storeId}/reviews")
	public CustomResponseEntity<Slice<StoreReviewResponse>> getStoreReview(@AuthUser User user,
		@PathVariable Long storeId,
		@RequestParam("type") Optional<ReviewType> reviewType,
		Pageable pageable) {
		return CustomResponseEntity.success(storeService.getStoreReview(user, storeId, reviewType, pageable));
	}

	@PostMapping("/stores/reviews")
	public CustomResponseEntity<ReviewAddResponse> createStoreReview(@AuthUser User user, @Valid @RequestBody
	ReviewRequest reviewRequest) {
		return CustomResponseEntity.created(storeService.createStoreReview(user, reviewRequest));
	}

	@GetMapping("/stores/{storeId}/reviews/check-limit")
	public CustomResponseEntity<ReviewAddLimitResponse> getUserDailyStoreReviewLimit(@AuthUser User user, @PathVariable Long storeId) {
		return CustomResponseEntity.success(storeService.checkUserDailyStoreReviewLimit(user, storeId));
	}

	// 리뷰 삭제
	@DeleteMapping("/reviews/{reviewId}")
    public CustomResponseEntity<Void> deleteStoreReview(@AuthUser User user,
            @PathVariable Long reviewId) {
        storeService.deleteStoreReview(user, reviewId);
        return CustomResponseEntity.success();
    }
}
