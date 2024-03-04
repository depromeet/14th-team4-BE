package com.depromeet.domains.store.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.S3.S3Service;
import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.bookmark.repository.BookmarkRepository;
import com.depromeet.domains.category.entity.Category;
import com.depromeet.domains.category.repository.CategoryRepository;
import com.depromeet.domains.review.entity.Review;
import com.depromeet.domains.review.repository.ReviewRepository;
import com.depromeet.domains.store.dto.request.NewStoreRequest;
import com.depromeet.domains.store.dto.request.ReviewRequest;
import com.depromeet.domains.store.dto.response.ReviewAddLimitResponse;
import com.depromeet.domains.store.dto.response.ReviewAddResponse;
import com.depromeet.domains.store.dto.response.StoreLocationRangeResponse;
import com.depromeet.domains.store.dto.response.StorePreviewResponse;
import com.depromeet.domains.store.dto.response.StoreReportResponse;
import com.depromeet.domains.store.dto.response.StoreReviewResponse;
import com.depromeet.domains.store.dto.response.StoreSharingSpotResponse;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.store.entity.StoreMeta;
import com.depromeet.domains.store.repository.storeMeta.StoreMetaRepository;
import com.depromeet.domains.store.repository.StoreRepository;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;
import com.depromeet.enums.CategoryType;
import com.depromeet.enums.ReviewType;
import com.depromeet.enums.UserLevel;
import com.depromeet.enums.ViewLevel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

	private final StoreRepository storeRepository;
	private final StoreMetaRepository storeMetaRepository;
	private final ReviewRepository reviewRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final BookmarkRepository bookmarkRepository;
	private final S3Service s3Service;

	// 음식점 프리뷰 조회(바텀 시트)
	@Transactional(readOnly = true)
	public StorePreviewResponse getStore(Long storeId, User user) {

		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));
		List<Review> reviews = reviewRepository.findTop10ByStoreOrderByVisitedAtDesc(store);

		ArrayList<String> reviewImageUrls = new ArrayList<>();
		for (Review review : reviews) {
			String imageUrl = review.getImageUrl();
			if (imageUrl != null) {
				reviewImageUrls.add(imageUrl);
			}
		}

		Long myRevisitedCount = reviewRepository.countByStoreAndUser(store, user);
		StoreMeta storeMeta = store.getStoreMeta();
		Long totalRevisitedCount = storeMeta.getTotalRevisitedCount();

		Boolean isBookmarked = false;
		if (bookmarkRepository.findByUserAndStore(user, store).isPresent()) {
			isBookmarked = true;
		}

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
			totalRevisitedCount,
			isBookmarked);
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

		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		Integer size = 10;
		Sort sort = Sort.by(Sort.Direction.DESC, "visitedAt");
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), size, sort);

		Slice<Review> reviews = null;
		if (reviewType.isEmpty()) {
			reviews = reviewRepository.findByStore(store, pageRequest);
		} else if (reviewType.get() == ReviewType.REVISITED) {
			reviews = reviewRepository.findRevisitedReviews(store, pageRequest);
		} else if (reviewType.get() == ReviewType.PHOTO) {
			reviews = reviewRepository.findByStoreAndImageUrlIsNotNullOrderByVisitedAtDesc(store, pageRequest);
		}

		// Review 객체를 StoreLogResponse DTO로 변환하여 Slice 객체에 담아 반환
		Slice<StoreReviewResponse> storeReviewResponse = getStoreReviewResponses(user, reviews, store);

		return storeReviewResponse;
	}

	private Slice<StoreReviewResponse> getStoreReviewResponses(User user, Slice<Review> reviews, Store store) {
		List<StoreReviewResponse> storeReviewResponseList = reviews.getContent().stream()
			.map(review -> {
				Integer maxVisitTimes = reviewRepository.maxVisitTimes(store, review.getUser());
				// 현재 사용자가 리뷰 작성자와 동일한지 확인
				Boolean isMine = review.getUser().getUserId().equals(user.getUserId()); // 사용자 비교 로직 수정
				String imageUrl = review.getImageUrl() != null ? review.getImageUrl() : "";
				// 필요한 정보를 포함하여 StoreReviewResponse 객체 생성
				return StoreReviewResponse.of(
					review.getUser().getUserId(),
					review.getReviewId(),
					review.getUser().getNickName(),
					review.getRating(),
					imageUrl,
					maxVisitTimes,
					review.getVisitedAt(),
					review.getDescription(),
					isMine
				);
			})
			.collect(Collectors.toList());

		Slice<StoreReviewResponse> storeReviewResponse = new SliceImpl<>(storeReviewResponseList, reviews.getPageable(),
			reviews.hasNext());
		return storeReviewResponse;
	}

	@Transactional(readOnly = true)
	public StoreLocationRangeResponse getRangeStores(Double leftTopLatitude, Double leftTopLongitude,
		Double rightBottomLatitude, Double rightBottomLongitude, Integer level, Optional<CategoryType> categoryType,
		User user) {

		List<StoreLocationRangeResponse.StoreLocationRange> totalList = new ArrayList<>();

		List<Store> bookMarkStoreList = this.storeRepository.findByUsersBookMarkList(user.getUserId());
		List<Long> bookMarkStoreIdList = storeToIdList(bookMarkStoreList);

		double maxLatitude = Double.max(leftTopLatitude, rightBottomLatitude);
		double minLatitude = Double.min(leftTopLatitude, rightBottomLatitude);
		double maxLongitude = Double.max(leftTopLongitude, rightBottomLongitude);
		double minLongitude = Double.min(leftTopLongitude, rightBottomLongitude);

		ViewLevel viewLevel = ViewLevel.findByLevel(level);
		CategoryType type = categoryType.isEmpty() ? null : CategoryType.findByType(categoryType.get().getType());

		List<Store> storeListWithCondition = this.storeRepository.findByLocationRangesWithCategory(maxLatitude,
			minLatitude, maxLongitude, minLongitude, type, bookMarkStoreIdList);

		if (bookMarkStoreIdList.size() == 0) {
			storeListWithCondition = this.storeRepository.findByLocationRangesWithCategoryNoExcept(maxLatitude,
				minLatitude, maxLongitude, minLongitude, type);
		}

		int viewStoreListCount = calculateViewStoreListRatio(storeListWithCondition.size(), viewLevel);

		totalList.addAll(toStoreLocationRange(bookMarkStoreList, true));
		totalList.addAll(toStoreLocationRange(storeListWithCondition.subList(0, viewStoreListCount), false));

		return toStoreLocationRangeResponse(totalList);
	}

	private int calculateViewStoreListRatio(int listSize, ViewLevel viewLevel) {
		if (viewLevel.getMinCount() >= listSize) {
			return listSize;
		}
		return (int)Math.round(listSize * viewLevel.getRatio());
	}

	private List<Long> storeToIdList(List<Store> storeList) {
		return storeList.stream()
			.map(store -> store.getStoreId())
			.collect(Collectors.toList());
	}

	private StoreLocationRangeResponse toStoreLocationRangeResponse(
		List<StoreLocationRangeResponse.StoreLocationRange> locationStoreList) {

		return StoreLocationRangeResponse.builder()
			.locationStoreList(locationStoreList)
			.build();
	}

	private List<StoreLocationRangeResponse.StoreLocationRange> toStoreLocationRange(List<Store> storeList,
		Boolean isBookmarked) {
		return storeList.stream()
			.map(store ->
				StoreLocationRangeResponse.StoreLocationRange.of(
					store.getStoreId(),
					store.getKakaoStoreId(),
					store.getStoreName(),
					store.getCategory().getCategoryId(),
					store.getCategory().getCategoryName(),
					store.getCategory().getCategoryType().getName(),
					store.getAddress(),
					store.getLocation().getLongitude(),
					store.getLocation().getLatitude(),
					store.getStoreMeta().getTotalRevisitedCount(),
					store.getStoreMeta().getTotalReviewCount(),
					isBookmarked))
			.collect(Collectors.toList());
	}

	private List<StoreSharingSpotResponse.StoreSharingSpot> toStoreSharingSpot(List<Store> storeList) {
		return storeList.stream()
			.map(store ->
				StoreSharingSpotResponse.StoreSharingSpot.of(
					store.getStoreId(),
					store.getKakaoStoreId(),
					store.getStoreName(),
					store.getCategory().getCategoryId(),
					store.getCategory().getCategoryName(),
					store.getCategory().getCategoryType().getName(),
					store.getAddress(),
					store.getLocation().getLongitude(),
					store.getLocation().getLatitude(),
					store.getStoreMeta().getTotalRevisitedCount(),
					store.getStoreMeta().getTotalReviewCount()))
			.collect(Collectors.toList());
	}

	@Transactional
	public synchronized ReviewAddResponse createStoreReview(User user, ReviewRequest reviewRequest) {
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
		user.increaseMyReviewCount();
		user.updateUserLevel(getUserLevel(user));
		userRepository.save(user);

		return ReviewAddResponse.of(review.getReviewId(), store.getStoreId());
	}

	private UserLevel getUserLevel(User user) {
		int userReviewCount = user.getMyReviewCount();
		if (userReviewCount >= 20) {
			return UserLevel.LEVEL4;
		}
		if (userReviewCount >= 6) {
			return UserLevel.LEVEL3;
		}
		if (userReviewCount >= 1) {
			return UserLevel.LEVEL2;
		}
		return UserLevel.LEVEL1;
	}

	private Store updateStoreMeta(Long storeId, User user, Integer rating) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		StoreMeta storeMeta = store.getStoreMeta();

		// 평점과 리뷰 개수 업데이트
		storeMeta.updateTotalRating(rating);
		storeMeta.increaseTotalReviewCount();

		// 사용자가 이 가게에 대해 작성한 리뷰 개수 확인
		Long userReviewCount = reviewRepository.countByStoreAndUser(store, user);
		if (userReviewCount == 1) {
			// 두 번째 리뷰인 경우, 재방문 횟수 증가
			storeMeta.increaseTotalRevisitedCount();
		}

		// 최다 방문자 횟수 업데이트
		storeMeta.updateMostRevisitedCount(userReviewCount + 1);
		storeMetaRepository.save(storeMeta);
		return store;
	}

	private Store createNewStoreWithMeta(NewStoreRequest newStoreRequest, Integer rating) {

		Store store = storeRepository.findByKakaoStoreId(newStoreRequest.getKakaoStoreId());
		if (store != null) {
			throw new CustomException(Result.DUPLICATED_STORE);
		}
		// store 객체 만들기
		store = buildNewStore(newStoreRequest);
		// storemeta 객체 초기화
		StoreMeta storeMeta = StoreMeta.builder()
			.totalRevisitedCount(0L)
			.totalReviewCount(1L)
			.mostVisitedCount(1L)
			.totalRating(rating.floatValue())
			.kakaoCategoryName(newStoreRequest.getKakaoCategoryName())
			.build();

		store.setStoreMeta(storeMeta);

		return store;
	}

	private Store buildNewStore(NewStoreRequest newStoreRequest) {
		String categoryName = newStoreRequest.getCategoryType(); // 예: "한식"

		CategoryType categoryType = CategoryType.fromDescription(categoryName)
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_CATEGORY));

		Category category = categoryRepository.findByCategoryType(categoryType)
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

	@Transactional
	public void deleteStoreReview(User user, Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_REVIEW));

		if (!review.getUser().getUserId().equals(user.getUserId())) {
			throw new CustomException(Result.UNAUTHORIZED_USER);
		}

		Store store = review.getStore();
		StoreMeta storeMeta = store.getStoreMeta();
		String imageUrl = review.getImageUrl();

		Long myRevisitedCount = reviewRepository.countByStoreAndUser(store, user); // 나의 재방문 횟수
		Long mostVisitedCount = storeMeta.getMostVisitedCount(); // 최다 방문 유저의 방문 수
		Long duplicateMostVisitedCount = reviewRepository.countByStoreAndReviewCount(store.getStoreId(),
			mostVisitedCount); // 최다 방문자의 인원수(최다 방문자가 여러명)

		boolean isDuplicateMostVisitor = duplicateMostVisitedCount > 1; // 최다 방문자가 여러명인지 여부
		boolean isMostVisitor = mostVisitedCount.equals(myRevisitedCount); // 내가 해당 음식점의 최다 방문자 인지 여부
		boolean hasVisitedThreeOrMore = myRevisitedCount >= 3; // 내 재방문 횟수가 3번 이상인지

		// 총 별점 및 전체 리뷰 개수 업데이트
		storeMeta.updateTotalRatingAfterDeletion(review.getRating());
		storeMeta.decreaseTotalReviewCount();

		if (isMostVisitor && isDuplicateMostVisitor) { // 나도 최다 방문자이고, 최다 방문자가 여러명인 경우
			log.info("나도 최다 방문자, 최다 방문자 여려명");
			processReviewForDuplicateMostVisitor(storeMeta, hasVisitedThreeOrMore);
			reviewRepository.delete(review);
			return;
		}

		if (isMostVisitor) { // 나만 최다 방문자인 경우
			log.info("나만 최다 방문자");
			processReviewForSingleMostVisitor(storeMeta, hasVisitedThreeOrMore);
			reviewRepository.delete(review);
			return;
		}

		// 내가 최다방문자가 아닌 경우
		log.info("내가 최다 방문자가 아니고, 최다 방문자가 여러명도 아닌 경우");
		processReviewForNonMostVisitor(storeMeta, hasVisitedThreeOrMore);
		reviewRepository.delete(review);

		user.decreaseMyReviewCount();

		if (storeMeta.getTotalReviewCount() == 0) {
			log.info("음식점도 삭제");
			storeRepository.delete(store);
		}

		if (imageUrl != null) {
			Integer lastIdx = imageUrl.lastIndexOf("/") + 1;
			String fileName = imageUrl.substring(lastIdx);
			s3Service.deleteFile(fileName);
		}
	}

	private void processReviewForDuplicateMostVisitor(StoreMeta storeMeta, boolean hasVisitedThreeOrMore) {
		if (hasVisitedThreeOrMore) { // 내 재방문 횟수가 3번 이상인 경우
			log.info("내 재방문 횟수가 3번 이상");
			return;
		}
		// 내 재방문 횟수가 3번 이상이 아닌 경우
		log.info("내 재방문 횟수가 3번 미만");
		// 최다 방문자의 재방문 횟수 유지, 전체 재방문 인원의 수 1 감소
		storeMeta.decreaseTotalRevisitCount();
	}

	private void processReviewForSingleMostVisitor(StoreMeta storeMeta, boolean hasVisitedThreeOrMore) {
		storeMeta.decreaseMostVisitedCount(); // 나만 최다 방문자이므로 최다 방문자의 재방문 횟수 1감소
		if (hasVisitedThreeOrMore) { // 내 재방문 횟수가 3번 이상인 경우
			log.info("내 재방문 횟수가 3번 이상");
			// 전체 재방문 인원 수 유지
			return;
		}
		// 내 재방문 횟수가 3번 이상이 아닌 경우
		log.info("내 재방문 횟수가 3번 미만");
		// 최다 방문자의 재방문 횟수 1감소, 전체 재방문 인원 수 1감소
		storeMeta.decreaseTotalRevisitCount();
	}

	private void processReviewForNonMostVisitor(StoreMeta storeMeta, boolean hasVisitedThreeOrMore) {
		if (hasVisitedThreeOrMore) { // 내 재방문 횟수가 3번 이상인 경우
			log.info("내 재방문 횟수가 3번 이상");
			// 최다 방문자의 재방문 횟수 유지, 전체 재방문 인원 수 유지
			return;
		}
		// 내 재방문 횟수가 3번 이상이 아닌 경우
		log.info("내 재방문 횟수가 3번 미만");
		// 최다 방문자의 재방문 횟수 유지, 전체 재방문 인원 수 1감소
		storeMeta.decreaseTotalRevisitCount();
	}

	public ReviewAddLimitResponse checkUserDailyStoreReviewLimit(User user, Long storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

		int reviewCount = reviewRepository.countStoreReviewByUserForDay(user, store, startOfDay, endOfDay);
		return ReviewAddLimitResponse.of(reviewCount < 3);
	}

	@Transactional(readOnly = true)
	public StoreSharingSpotResponse getSharingSpots(Long userId) {

		User user = this.userRepository.findById(userId).orElseThrow(
			() -> new CustomException(Result.NOT_FOUND_USER));

		List<Review> reviews = this.reviewRepository.findByUser(user);

		// 재방문이상(리뷰 수 2개 이상)의 식당만 공유
		List<Store> revisitedStores = reviews.stream()
			.collect(Collectors.groupingBy(Review::getStore))
			.entrySet()
			.stream()
			.filter(entry -> entry.getValue().size() >= 2)
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());

		return StoreSharingSpotResponse.of(user.getNickName(), toStoreSharingSpot(revisitedStores));
	}
}
