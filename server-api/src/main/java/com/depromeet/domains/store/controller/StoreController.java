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

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.store.dto.request.FeedRequest;
import com.depromeet.domains.store.dto.response.FeedAddResponse;
import com.depromeet.domains.store.dto.response.ReviewAddLimitResponse;
import com.depromeet.domains.store.dto.response.StoreLocationRangeResponse;
import com.depromeet.domains.store.dto.response.StorePreviewResponse;
import com.depromeet.domains.store.dto.response.StoreReportResponse;
import com.depromeet.domains.store.dto.response.StoreReviewResponse;
import com.depromeet.domains.store.dto.response.StoreSharingSpotResponse;
import com.depromeet.domains.store.service.StoreService;
import com.depromeet.domains.user.entity.User;
import com.depromeet.enums.CategoryType;
import com.depromeet.enums.ReviewType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@GetMapping("/stores/location-range")
	public CustomResponseEntity<StoreLocationRangeResponse> getLocationRangeStores(
		@RequestParam(value = "leftTopLatitude") Double leftTopLatitude,
		@RequestParam(value = "leftTopLongitude") Double leftTopLongitude,
		@RequestParam(value = "rightBottomLatitude") Double rightBottomLatitude,
		@RequestParam(value = "rightBottomLongitude") Double rightBottomLongitude,
		@RequestParam(value = "level") Integer level,
		@RequestParam(value = "type") Optional<CategoryType> categoryType,
		@AuthUser User user) {

		return CustomResponseEntity.success(storeService.getRangeStores(leftTopLatitude, leftTopLongitude,
			rightBottomLatitude, rightBottomLongitude,
			level, categoryType,
			user));
	}

	@GetMapping("/stores/sharing-spot")
	public CustomResponseEntity<StoreSharingSpotResponse> getSharingSpots
		(@RequestParam(value = "userId") Long userId) {
		return CustomResponseEntity.success(storeService.getSharingSpots(userId));
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

	@PostMapping("/stores/feeds")
	public CustomResponseEntity<FeedAddResponse> createStoreFeed(@AuthUser User user, @Valid @RequestBody
	FeedRequest feedRequest) {
		return CustomResponseEntity.created(storeService.createStoreFeed(user, feedRequest));
	}

	@GetMapping("/stores/{storeId}/reviews/check-limit")
	public CustomResponseEntity<ReviewAddLimitResponse> getUserDailyStoreReviewLimit(@AuthUser User user,
		@PathVariable Long storeId) {
		return CustomResponseEntity.success(storeService.checkUserDailyStoreReviewLimit(user, storeId));
	}
}
