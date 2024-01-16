package com.depromeet.domains.store.service;

import java.util.*;
import java.util.stream.Collectors;

import com.depromeet.domains.category.repository.CategoryRepository;
import com.depromeet.domains.store.entity.StoreMeta;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.category.entity.Category;
import com.depromeet.domains.review.entity.Review;
import com.depromeet.domains.review.repository.ReviewRepository;
import com.depromeet.domains.store.dto.StoreLocationDto;
import com.depromeet.domains.store.dto.request.NewStoreRequest;
import com.depromeet.domains.store.dto.request.ReviewRequest;
import com.depromeet.domains.store.dto.request.StoreLocationRangeRequest;
import com.depromeet.domains.store.dto.response.ReviewAddResponse;
import com.depromeet.domains.store.dto.response.StoreLocationRangeResponse;
import com.depromeet.domains.store.dto.response.StorePreviewResponse;
import com.depromeet.domains.store.dto.response.StoreReportResponse;
import com.depromeet.domains.store.dto.response.StoreReviewResponse;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.store.repository.StoreRepository;
import com.depromeet.domains.user.entity.User;
import com.depromeet.enums.ReviewType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final ReviewRepository reviewRepository;
	private final CategoryRepository categoryRepository;

	// 음식점 프리뷰 조회(바텀 시트)
	@Transactional(readOnly = true)
	public StorePreviewResponse getStore(Long storeId, User user) {

		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));
		List<Review> reviews = reviewRepository.findTop10ByStoreOrderByCreatedAtDesc(store);

		ArrayList<String> reviewImageUrls = new ArrayList<>();
		for (Review review : reviews) {
			reviewImageUrls.add(review.getImageUrl());
		}

		Long myRevisitedCount = reviewRepository.countByStoreAndUser(store, user);
		StoreMeta storeMeta = store.getStoreMeta();
		Long totalRevisitedCount = storeMeta.getTotalRevisitedCount();

		return StorePreviewResponse.of(
			store.getStoreId(),
			store.getCategory().getCategoryName(),
			store.getStoreName(),
			store.getAddress(),
			storeMeta.getTotalRating(),
			storeMeta.getTotalReviewCount(),
			reviewImageUrls,
			user.getUserId(),
			myRevisitedCount,
			totalRevisitedCount);
	}

	// 음식점 상세조회시 또잇 리포트 조회
	@Transactional(readOnly = true)
	public StoreReportResponse getStoreReport(Long storeId) {
		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		StoreMeta storeMeta = store.getStoreMeta();

		Long mostVisitedCount = storeMeta.getMostVisitedCount();
		Long totalRevisitedCount = storeMeta.getTotalRevisitedCount();

		return StoreReportResponse.of(
			store.getStoreId(),
			store.getThumbnailUrl(),
			mostVisitedCount,
			totalRevisitedCount);
	}

	// 음식점 리뷰 조회(타입별 조회)
	@Transactional(readOnly = true)
	public Slice<StoreReviewResponse> getStoreReview(User user, Long storeId, Optional<ReviewType> reviewType, Pageable pageable) {

		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		List<Review> reviews = new ArrayList<>();
		if(reviewType.isEmpty()){
			reviews = reviewRepository.findByStore(store);
		}
		else if (reviewType.get() == ReviewType.REVISITED) {
			reviews = reviewRepository.findRevisitedReviews(store);
		} else if (reviewType.get() == ReviewType.PHOTO) {
			reviews = reviewRepository.findByImageUrlIsNotNullOrderByCreatedAtDesc();
		}

		// Review 객체를 StoreLogResponse DTO로 변환
		List<StoreReviewResponse> storeReviewResponse = getStoreReviewResponses(user, reviews);

		// Slice 객체 생성
		return new SliceImpl<>(storeReviewResponse, pageable, storeReviewResponse.size() == pageable.getPageSize());
	}

	private static List<StoreReviewResponse> getStoreReviewResponses(User user, List<Review> reviews) {
		List<StoreReviewResponse> storeReviewResponse = reviews.stream()
				.map(review -> {
					// 현재 사용자가 리뷰 작성자와 동일한지 확인
					Boolean isMine = review.getUser().getUserId().equals(user);

					// 필요한 정보를 포함하여 StoreReviewResponse 객체 생성
					return StoreReviewResponse.of(
							review.getUser().getUserId(),
							review.getReviewId(),
							review.getUser().getNickName(),
							review.getRating(),
							review.getImageUrl(),
							review.getVisitTimes(),
							review.getVisitedAt(),
							review.getDescription(),
							isMine
					);
				})
				.collect(Collectors.toList());
		return storeReviewResponse;
	}

	@Transactional(readOnly = true)
	public StoreLocationRangeResponse getRangeStores(StoreLocationRangeRequest location1,
		StoreLocationRangeRequest location2, Long userId) {

		Double maxLatitude = Double.max(location1.getLatitude(), location2.getLatitude());
		Double minLatitude = Double.min(location1.getLatitude(), location2.getLatitude());
		Double maxLongitude = Double.max(location1.getLongitude(), location2.getLongitude());
		Double minLongitude = Double.min(location1.getLongitude(), location2.getLongitude());

		// 특정 위, 경도 범위 안에 있는 식당 정보 + 해당 user의 북마크 여부
		List<Object[]> locationRangesQueryResult =
			storeRepository.findByLocationRangesWithIsBookmark(maxLatitude, minLatitude, maxLongitude, minLongitude,
				userId);

		List<StoreLocationDto> locationWithIsBookmarkList = convertToStoreLocationDto(locationRangesQueryResult);

		List<Long> storeIdListWithinRanges = locationWithIsBookmarkList.stream()
			.map(StoreLocationDto::getStoreId)
			.collect(Collectors.toList());

		// 위에서 조회한 식당들의 재방문한 사람들의 수
		List<Object[]> storesWithRevisitedNumberQueryResult = storeRepository.findByStoresWithNumberOfRevisitedUser(
			storeIdListWithinRanges);

		return makeStoreLocationRangeResponse(locationWithIsBookmarkList,
			convertToRevisitedNumberMap(storesWithRevisitedNumberQueryResult));
	}

	private StoreLocationRangeResponse makeStoreLocationRangeResponse(List<StoreLocationDto> locationWithIsBookmarkList,
		Map<Long, Integer> revisitedNumberMap) {

		List<StoreLocationRangeResponse.LocationRangeResponse> result = locationWithIsBookmarkList.stream()
			.map(row -> StoreLocationRangeResponse.LocationRangeResponse.of(
				row.getStoreId(),
				row.getStoreName(),
				row.getLongitude(),
				row.getLatitude(),
				row.isBookMarked(),
				revisitedNumberMap.get(row.getStoreId()))).collect(Collectors.toList());

		return StoreLocationRangeResponse.of(result);
	}

	private Map<Long, Integer> convertToRevisitedNumberMap(List<Object[]> storesWithRevisitedNumberQueryResult) {
		Map<Long, Integer> revisitedNumberMap = new HashMap<>();

		if (!ObjectUtils.isEmpty(storesWithRevisitedNumberQueryResult)) {
			storesWithRevisitedNumberQueryResult.stream()
				.forEach(row -> {
						Long storeId = (Long)row[0];
						int numberOfRevisitedUser = Integer.parseInt(String.valueOf(row[2]));

						revisitedNumberMap.put(storeId, numberOfRevisitedUser);
					}
				);
		}

		return revisitedNumberMap;
	}

	private List<StoreLocationDto> convertToStoreLocationDto(List<Object[]> nativeQueryResult) {
		List<StoreLocationDto> result = new ArrayList<>();
		if (!ObjectUtils.isEmpty(nativeQueryResult)) {
			result = nativeQueryResult.stream()
				.map(row -> {
					Long storeId = (Long)row[0];
					String storeName = (String)row[1];
					Double latitude = (Double)row[2];
					Double longitude = (Double)row[3];
					Long isBookMarkedLongVal = (Long)row[4];
					boolean isBookmarkedBoolean = false;
					if (!ObjectUtils.isEmpty(isBookMarkedLongVal) && isBookMarkedLongVal.equals(1L)) {
						isBookmarkedBoolean = true;
					}

					return StoreLocationDto.of(storeId, storeName, latitude, longitude, isBookmarkedBoolean);
				})
				.collect(Collectors.toList());
		}

		return result;
	}


	@Transactional
	public ReviewAddResponse createStoreReview(User user, ReviewRequest reviewRequest) {
		Store store = getOrSaveStore(reviewRequest);
		Review review = saveReview(user, store, reviewRequest);
		store.updateStoreSummary(reviewRequest.getRating());
		store.updateThumnailUrl(reviewRequest.getImageUrl());
		return ReviewAddResponse.of(review.getReviewId(), store.getStoreId());
	}

	private Store getOrSaveStore(ReviewRequest reviewRequest) {
		if (reviewRequest.getStoreId() != null) {
			return storeRepository.findById(reviewRequest.getStoreId())
				.orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));
		}
		return saveStore(reviewRequest.getNewStore());
	}

	private int getVisitTimes(Long storeId, Store store, User user) {
		return storeId != null
			? reviewRepository.countByStoreAndUser(store, user).intValue() + 1
			: 1;
	}

	private Store saveStore(NewStoreRequest newStore) {
		Category category = categoryRepository.findById(newStore.getCategoryId())
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_CATEGORY));
		Store store = newStore.toEntity(category);
		storeRepository.save(store);

		return store;
	}

	private Review saveReview(User user, Store store, ReviewRequest reviewRequest) {
		int visitTimes = getVisitTimes(reviewRequest.getStoreId(), store, user);
		Review review = reviewRequest.toEntity(store, user, visitTimes);
		reviewRepository.save(review);
		return review;
	}

//	public void deleteStoreReview(User user, Long storeId, Long reviewId) {
//		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));
//		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_REVIEW));
//
//		if (!review.getUser().getUserId().equals(user.getUserId())) {
//			throw new CustomException(Result.UNAUTHORIZED_USER);
//		}
//		reviewRepository.delete(review);
//		store.updateStoreSummary(-review.getRating());
//	}
}
