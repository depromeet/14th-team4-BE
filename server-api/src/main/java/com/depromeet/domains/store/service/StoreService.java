package com.depromeet.domains.store.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.depromeet.domains.store.dto.StoreFeedResponse;
import com.depromeet.domains.store.dto.response.*;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.S3.S3Service;
import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.bookmark.entity.Bookmark;
import com.depromeet.domains.bookmark.repository.BookmarkRepository;
import com.depromeet.domains.feed.entity.Feed;
import com.depromeet.domains.feed.repository.FeedRepository;
import com.depromeet.domains.store.dto.request.FeedRequest;
import com.depromeet.domains.store.dto.request.NewStoreRequest;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.store.repository.StoreRepository;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;
import com.depromeet.enums.UserLevel;
import com.depromeet.enums.ViewLevel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.depromeet.common.CursorPagingCommon.getSlice;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

	private final StoreRepository storeRepository;
	private final FeedRepository feedRepository;
	private final UserRepository userRepository;
	private final BookmarkRepository bookmarkRepository;
	private final S3Service s3Service;

	// 음식점 프리뷰 조회(바텀 시트)
	@Transactional(readOnly = true)
	public StorePreviewResponse getStore(Long storeId, User user) {

		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));
		List<String> feedImageUrls = feedRepository.findTop10FeedImagesByCreatedAtDesc(store.getStoreId());

		Boolean isBookmarked = false;
		if (bookmarkRepository.findByUserIdAndStoreId(user.getUserId(), store.getStoreId()).isPresent()) {
			isBookmarked = true;
		}

		return StorePreviewResponse.of(
			store.getStoreId(),
			store.getKakaoCategoryName(),
			store.getStoreName(),
			store.getAddress(),
			store.getTotalRating(),
			store.getTotalFeedCnt(),
			feedImageUrls,
			user.getUserId(),
			isBookmarked);
	}

	// 음식점 상세조회시 또잇 리포트 조회
	@Transactional(readOnly = true)
	public StoreReportResponse getStoreReport(Long storeId) {
		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		return StoreReportResponse.of(
			store.getStoreId(),
			store.getThumbnailUrl(),
			null, null);
	}

	// 음식점 리뷰 조회(타입별 조회)
	@Transactional(readOnly = true)
	public Slice<StoreFeedResponse> getStoreFeed(User user, Long storeId,
												 Long lastIdxId, Integer size) {

		Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));
		List<StoreFeedResponse> storeFeedResponses = feedRepository.findFeedByStoreId(storeId, user.getUserId(), lastIdxId, size);
		Slice<StoreFeedResponse> response = getSlice(storeFeedResponses, size);
		return response;
	}

	@Transactional(readOnly = true)
	public StoreLocationRangeResponse getRangeStores(Double leftTopLatitude, Double leftTopLongitude,
		Double rightBottomLatitude, Double rightBottomLongitude, Integer level, User user) {

		List<StoreLocationRangeResponse.StoreLocationRange> totalList = new ArrayList<>();

		List<Bookmark> bookmarks = this.bookmarkRepository.findByUserId(user.getUserId());
		List<Long> bookMarkStoreIdList = bookmarks.stream()
			.map(Bookmark::getStoreId)
			.collect(Collectors.toList());

		List<Store> bookMarkStoreList = this.storeRepository.findByStoreIdList(bookMarkStoreIdList);

		double maxLatitude = Double.max(leftTopLatitude, rightBottomLatitude);
		double minLatitude = Double.min(leftTopLatitude, rightBottomLatitude);
		double maxLongitude = Double.max(leftTopLongitude, rightBottomLongitude);
		double minLongitude = Double.min(leftTopLongitude, rightBottomLongitude);

		ViewLevel viewLevel = ViewLevel.findByLevel(level);

		List<Store> storeListWithCondition
			= this.storeRepository.findByLocationRangesNotInBookmarks(maxLatitude,
			minLatitude, maxLongitude, minLongitude, bookMarkStoreIdList);

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
					store.getAddress(),
					store.getLocation().getLongitude(),
					store.getLocation().getLatitude(),
					store.getTotalFeedCnt(),
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
					store.getAddress(),
					store.getLocation().getLongitude(),
					store.getLocation().getLatitude(),
					store.getTotalFeedCnt()))
			.collect(Collectors.toList());
	}

	@Transactional
	public synchronized FeedAddResponse createStoreFeed(User user, FeedRequest feedRequest) {
		Store store = processStore(feedRequest);
		Feed feed = feedRepository.save(feedRequest.toEntity(user));

		return FeedAddResponse.of(feed.getFeedId(), store.getStoreId());
	}

	private void updateUserProfile(User user) {
		user.increaseMyFeedCount();
		user.updateUserLevel(getUserLevel(user));
		userRepository.save(user);
	}

	private Store processStore(FeedRequest feedRequest) {
		Store store = feedRequest.hasStoreId()
			? updateStoreInfo(feedRequest)
			: createNewStore(feedRequest);

		return storeRepository.save(store);
	}

	private UserLevel getUserLevel(User user) {
		int userMyFeedCnt = user.getMyFeedCnt();
		if (userMyFeedCnt >= 20) {
			return UserLevel.LEVEL4;
		}
		if (userMyFeedCnt >= 6) {
			return UserLevel.LEVEL3;
		}
		if (userMyFeedCnt >= 1) {
			return UserLevel.LEVEL2;
		}
		return UserLevel.LEVEL1;
	}

	private Store updateStoreInfo(FeedRequest feedRequest) {
		Store store = storeRepository.findById(feedRequest.getStoreId())
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		// 평점과 리뷰 개수 업데이트
		store.updateTotalRating(feedRequest.getRating());
		store.increaseTotalFeedCnt();
		;
		return store;
	}

	private Store createNewStore(FeedRequest feedRequest) {
		NewStoreRequest newStore = feedRequest.getNewStore();
		Store store = storeRepository.findByKakaoStoreId(newStore.getKakaoStoreId());
		if (store != null) {
			throw new CustomException(Result.DUPLICATED_STORE);
		}
		return storeRepository.save(newStore.toEntity(feedRequest.getRating(), feedRequest.getImageUrl()));
	}

	private Feed saveFeed(Feed feed) {
		return feedRepository.save(feed);
	}


	public FeedAddLimitResponse checkUserDailyStoreFeedLimit(User user, Long storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

		Long feedCount = feedRepository.countStoreReviewByUserForDay(user.getUserId(), store.getStoreId(), startOfDay,
			endOfDay);
		return FeedAddLimitResponse.of(feedCount < 3);
	}

	@Transactional(readOnly = true)
	public StoreSharingSpotResponse getSharingSpots(Long userId) {

		User user = this.userRepository.findById(userId).orElseThrow(
			() -> new CustomException(Result.NOT_FOUND_USER));

		List<Feed> feeds = this.feedRepository.findByUserId(user.getUserId());

		List<Store> storesMoreThanTwoFeeds = feeds.stream()
			.map(feed -> storeRepository.findById(feed.getStoreId())
				.orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE)))
			.filter(store -> store.getTotalFeedCnt() >= 2)
			.collect(Collectors.toList());

		return StoreSharingSpotResponse.of(user.getNickName(), toStoreSharingSpot(storesMoreThanTwoFeeds));
	}
}
