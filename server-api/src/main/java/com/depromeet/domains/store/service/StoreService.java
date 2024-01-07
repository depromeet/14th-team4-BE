package com.depromeet.domains.store.service;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.review.entity.Review;
import com.depromeet.domains.review.repository.ReviewRepository;
import com.depromeet.domains.store.dto.response.StorePreviewResponse;
import com.depromeet.domains.store.dto.response.StoreReportResponse;
import com.depromeet.domains.store.dto.response.StoreReviewResponse;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.store.repository.StoreRepository;
import com.depromeet.domains.user.entity.User;
import com.depromeet.enums.ReviewType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final ReviewRepository reviewRepository;

	@Transactional(readOnly = true)
	public StorePreviewResponse getStore(Long storeId, User user) {

		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));
		List<Review> reviews = reviewRepository.findTop10ByStoreOrderByCreatedAtDesc(store);

		ArrayList<String> logImageUrls = new ArrayList<>();
		for (Review review : reviews) {
			logImageUrls.add(review.getImageUrl());
		}

		Long revisitedCount = reviewRepository.countByStoreAndUser(store, user);
		Long totalRevisitedCount = reviewRepository.countTotalRevisitedCount(store);

		return StorePreviewResponse.of(
				store.getStoreId(),
				store.getCategory().getCategoryId(),
				store.getStoreName(),
				store.getRoadAddress(),
				store.getTotalRating(),
				store.getTotalReviewCount(),
				logImageUrls,
				user.getUserId(),
				revisitedCount,
				totalRevisitedCount);
	}

	@Transactional(readOnly = true)
	public StoreReportResponse getStoreReport(Long storeId) {
		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		Long mostVisitedCount = reviewRepository.maxReviewCount(store);
		Long totalRevisitedCount = reviewRepository.countTotalRevisitedCount(store);

		return StoreReportResponse.of(
				store.getStoreId(),
				store.getThumbnailUrl(),
				mostVisitedCount,
				totalRevisitedCount);
	}

	@Transactional(readOnly = true)
	public Slice<StoreReviewResponse> getStoreReview(Long storeId, ReviewType reviewType, Pageable pageable) {

		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		List<Review> reviews = new ArrayList<>();
		if (reviewType==ReviewType.REVISITED) {
			reviews = reviewRepository.findRevisitedReviews(store);
		} else if (reviewType==ReviewType.PHOTO) {
			reviews = reviewRepository.findByImageUrlIsNotNullOrderByCreatedAtDesc();
		}

		// Review 객체를 StoreLogResponse DTO로 변환
		List<StoreReviewResponse> storeReviewResponse = getStoreLogResponses(reviews);

		// Slice 객체 생성
		return new SliceImpl<>(storeReviewResponse, pageable, storeReviewResponse.size() == pageable.getPageSize());
	}

	private static List<StoreReviewResponse> getStoreLogResponses(List<Review> reviews) {
		List<StoreReviewResponse> storeLogResponses = reviews.stream()
				.map(review -> StoreReviewResponse.of(
						review.getUser().getUserId(),
						review.getUser().getNickName(),
						review.getRating(),
						review.getImageUrl(),
						review.getVisitTimes(),
						review.getVisitedAt(),
						review.getDescription()))
				.collect(Collectors.toList());
		return storeLogResponses;
	}
}
