package com.depromeet.domains.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.depromeet.domains.review.entity.Review;
import com.depromeet.domains.review.repository.ReviewRepository;
import com.depromeet.domains.store.dto.StoreLocationDto;
import com.depromeet.domains.store.dto.request.StoreLocationRangeRequest;
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
			store.getStoreAddress().getRoadAddress(),
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
		if (reviewType == ReviewType.REVISITED) {
			reviews = reviewRepository.findRevisitedReviews(store);
		} else if (reviewType == ReviewType.PHOTO) {
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

	@Transactional(readOnly = true)
	public StoreLocationRangeResponse getRangeStores(StoreLocationRangeRequest location1,
		StoreLocationRangeRequest location2, Long userId) {

		Double maxLatitude = Double.max(location1.getLatitute(), location2.getLatitute());
		Double minLatitude = Double.min(location1.getLatitute(), location2.getLatitute());
		Double maxLongitude = Double.max(location1.getLongtitue(), location2.getLongtitue());
		Double minLongitude = Double.min(location1.getLongtitue(), location2.getLongtitue());

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

}
