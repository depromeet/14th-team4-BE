package com.depromeet.domains.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.depromeet.domains.store.entity.StoreMeta;
import com.depromeet.domains.store.repository.StoreMetaRepository;
import com.depromeet.domains.store.repository.StoreRepository;
import com.depromeet.domains.user.entity.User;
import com.depromeet.enums.ReviewType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final StoreMetaRepository storeMetaRepository;
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
	public Slice<StoreReviewResponse> getStoreReview(User user, Long storeId, Optional<ReviewType> reviewType,
		Pageable pageable) {

		Sort sort = Sort.by(Sort.Direction.DESC, "visitedAt");
		PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		List<Review> reviews = new ArrayList<>();
		if (reviewType.isEmpty()) {
			reviews = reviewRepository.findByStore(store);
		} else if (reviewType.get() == ReviewType.REVISITED) {
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

		// Double maxLatitude = Double.max(location1.getLatitude(), location2.getLatitude());
		// Double minLatitude = Double.min(location1.getLatitude(), location2.getLatitude());
		// Double maxLongitude = Double.max(location1.getLongitude(), location2.getLongitude());
		// Double minLongitude = Double.min(location1.getLongitude(), location2.getLongitude());
		//
		// // 특정 위, 경도 범위 안에 있는 식당 정보 + 해당 user의 북마크 여부
		// List<Object[]> locationRangesQueryResult =
		// 	storeRepository.findByLocationRangesWithIsBookmark(maxLatitude, minLatitude, maxLongitude, minLongitude,
		// 		userId);
		//
		// List<StoreLocationDto> locationWithIsBookmarkList = convertToStoreLocationDto(locationRangesQueryResult);
		//
		// List<Long> storeIdListWithinRanges = locationWithIsBookmarkList.stream()
		// 	.map(StoreLocationDto::getStoreId)
		// 	.collect(Collectors.toList());
		//
		// // 위에서 조회한 식당들의 재방문한 사람들의 수
		// List<Object[]> storesWithRevisitedNumberQueryResult = storeRepository.findByStoresWithNumberOfRevisitedUser(
		// 	storeIdListWithinRanges);
		//
		// return makeStoreLocationRangeResponse(locationWithIsBookmarkList,
		// 	convertToRevisitedNumberMap(storesWithRevisitedNumberQueryResult));
		return null;
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
		Store store;
		if (reviewRequest.getStoreId() != null) {
			// 기존 StoreMeta 정보 업데이트
			store = updateStoreMeta(reviewRequest.getStoreId(), user, reviewRequest.getRating());
			if (store.getThumbnailUrl() == null && reviewRequest.getImageUrl() != null) {
				store.setThumbnailUrl(reviewRequest.getImageUrl());
			}
		} else {
			// 새로운 Store 생성
			store = createNewStoreWithMeta(reviewRequest.getNewStore(), reviewRequest.getRating());
			if (reviewRequest.getImageUrl() != null) {
				store.setThumbnailUrl(reviewRequest.getImageUrl());
			}
		}
		storeRepository.save(store);
		Review review = saveReview(user, store, reviewRequest);

		// store.updateStoreSummary(reviewRequest.getRating());
		store.updateThumbnailUrl(reviewRequest.getImageUrl());
		return ReviewAddResponse.of(review.getReviewId(), store.getStoreId());
	}

	private Store updateStoreMeta(Long storeId, User user, Integer rating) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		StoreMeta storeMeta = store.getStoreMeta();

		// 평점과 리뷰 개수 업데이트
		storeMeta.updateTotalRating(rating);

		// 사용자가 이 가게에 대해 작성한 리뷰 개수 확인
		Long userReviewCount = reviewRepository.countByStoreAndUser(store, user);
		if (userReviewCount == 1) {
			// 두 번째 리뷰인 경우, 재방문 횟수 증가
			storeMeta.incrementTotalRevisitedCount();
		}

		// 최다 방문자 횟수 업데이트
		storeMeta.updateMostRevisitedCount(userReviewCount);
		storeMetaRepository.save(storeMeta);
		return store;
	}

	private Store createNewStoreWithMeta(NewStoreRequest newStoreRequest, Integer rating) {
		// store 객체 만들기
		Store store = buildNewStore(newStoreRequest);
		// storemeta 객체 초기화
		StoreMeta storeMeta = StoreMeta.builder()
			.totalRevisitedCount(0L)
			.totalReviewCount(1L)
			.mostVisitedCount(0L)
			.totalRating(rating.floatValue())
			.build();

		store.setStoreMeta(storeMeta);

		return store;
	}

	private Store buildNewStore(NewStoreRequest newStoreRequest) {
		Category category = categoryRepository.findById(newStoreRequest.getCategoryId())
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_CATEGORY));
		return newStoreRequest.toEntity(category);
	}

	private int getVisitTimes(Long storeId, Store store, User user) {
		return storeId != null
			? reviewRepository.countByStoreAndUser(store, user).intValue() + 1
			: 1;
	}

	private Review saveReview(User user, Store store, ReviewRequest reviewRequest) {
		int visitTimes = getVisitTimes(reviewRequest.getStoreId(), store, user);
		Review review = reviewRequest.toEntity(store, user, visitTimes);
		reviewRepository.save(review);
		return review;
	}

	public void deleteStoreReview(User user, Long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_REVIEW));

		if (!review.getUser().getUserId().equals(user.getUserId())) {
			throw new CustomException(Result.UNAUTHORIZED_USER);
		}

		Store store = review.getStore();
		StoreMeta storeMeta = store.getStoreMeta();

		// 해당 음식점에 몇번 방문했는지 확인
		Long myRevisitedCount = reviewRepository.countByStoreAndUser(store, user);

		// 해당 음식점 최다 방문자인지 확인
		if (storeMeta.getMostVisitedCount() ==  myRevisitedCount) {
			// 촤다 방문자가 겹치는 경우에 몇명이나 최다 방문자가 있는지 확인
			Long duplicateMostVisitedCount = reviewRepository.countByVisitTimes(myRevisitedCount);
			if (duplicateMostVisitedCount > 1) { // 최다 방문자가 여러명인 경우
				if (myRevisitedCount >= 3) { // 내가 쓴 리뷰의 개수가 3개 이상이면
					// 최다 방문자의 재방문 횟수는 그대로 유지, 재방문 인원의 수도 유지, 리뷰 개수 1감소, 별점 평균 재계산
					storeMeta.deletedReviewFromVisitedThreeOrMoreIfMostVisitorDuplicate(review.getRating());
				} else { // 내가 쓴 리뷰가 2개 이하인 경우
					// 최다 방문자의 재방문 횟수는 그대로 유지, 재방문 인원의 수 1감소, 리뷰 개수 1감소, 별점 평균 재계산
					storeMeta.deletedReviewFromVisitedTwoOrLessIfMostVisitorDuplicate(review.getRating());
				}
			}
			else { // 최다 방문자가 나 혼자인 경우
				if (myRevisitedCount >= 3) { // 내가 쓴 리뷰의 개수가 3개 이상이면
					// 최다 방문자의 재방문 횟수는 1 감소, 재방문 인원의 수도 유지, 리뷰 개수 1감소, 별점 평균 재계산
					storeMeta.deleteReviewFromVisitedThreeOrMoreIfMostVisitorMe(review.getRating());
				} else { // 내가 쓴 리뷰가 2개 이하인 경우
					// 최다 방문자의 재방문 횟수, 재방문 인원의 수, 리뷰 개수 모두 1감소, 별점 평균 재계산
					storeMeta.deletedReviewFromVisitedTwoOrLessIfMostVisitorMe(review.getRating());
				}
			}
		}
		else{ // 최다 방문자가 아닌 경우
				if (myRevisitedCount>=3){ // 내가 쓴 리뷰의 개수가 3개 이상이면
					// 재방문 인원의 수는 유지, 리뷰 개수 1감소, 별점 평균 재계산
					storeMeta.deleteReviewFromVisitedThreeOrMore(review.getRating());
				}
				else {// 내가 쓴 리뷰의 개수가 2개 이하인 경우
					// 재방문 인원의 수, 리뷰 개수 모두 1 감소, 별점 평균 재계산
					storeMeta.deleteReviewFromVisitedTwoOrLess(review.getRating());
				}
			}
		reviewRepository.delete(review);
	}
}
