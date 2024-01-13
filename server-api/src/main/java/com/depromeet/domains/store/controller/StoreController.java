package com.depromeet.domains.store.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.store.dto.request.ReviewRequest;
import com.depromeet.domains.store.dto.response.ReviewAddResponse;
import com.depromeet.domains.store.dto.request.StoreLocationRangeRequest;
import com.depromeet.domains.store.dto.response.StoreLocationRangeResponse;
import com.depromeet.domains.store.dto.response.StorePreviewResponse;
import com.depromeet.domains.store.dto.response.StoreReportResponse;
import com.depromeet.domains.store.dto.response.StoreReviewResponse;
import com.depromeet.domains.store.service.StoreService;
import com.depromeet.domains.user.entity.User;
import com.depromeet.enums.ReviewType;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/stores/location-range")
    public CustomResponseEntity<StoreLocationRangeResponse> getStores(
            @RequestParam("location1") StoreLocationRangeRequest location1,
            @RequestParam("location2") StoreLocationRangeRequest location2,
            @AuthUser User user) {
        return CustomResponseEntity.success(storeService.getRangeStores(location1, location2, user.getUserId()));
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
	public CustomResponseEntity<ReviewAddResponse> createStoreReview(@AuthUser User user, @RequestBody
	ReviewRequest reviewRequest) {
		return CustomResponseEntity.created(storeService.createStoreReview(user, reviewRequest));
	}
}
