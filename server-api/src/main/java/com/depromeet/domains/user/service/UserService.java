package com.depromeet.domains.user.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.depromeet.auth.service.RedisService;
import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.bookmark.entity.Bookmark;
import com.depromeet.domains.bookmark.repository.BookmarkRepository;
import com.depromeet.domains.feed.entity.Feed;
import com.depromeet.domains.feed.repository.FeedRepository;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.store.repository.StoreRepository;
import com.depromeet.domains.user.dto.response.UserBookmarkResponse;
import com.depromeet.domains.user.dto.response.UserProfileResponse;
import com.depromeet.domains.user.dto.response.UserFeedResponse;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
	private final RedisService redisService;
	private final UserRepository userRepository;
	private final BookmarkRepository bookmarkRepository;
	private final FeedRepository feedRepository;
	private final StoreRepository storeRepository;

	@Transactional
	public void updateUserNickname(User user, String nickname) {
		User existUser = findUserById(user.getUserId());
		if (userRepository.existsByNickName(nickname)) {
			throw new CustomException(Result.DUPLICATED_NICKNAME);
		}
		existUser.updateNickname(nickname);
	}

	@Transactional(readOnly = true)
	public UserProfileResponse getUserProfile(User user) {
		User existUser = findUserById(user.getUserId());
		return UserProfileResponse.of(existUser.getUserId(), existUser.getNickName(), existUser.getLevel().getDescription());
	}

	@Transactional(readOnly = true)
	public Slice<UserBookmarkResponse> getUserBookmarks(User user, Pageable pageable) {
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
		Slice<Bookmark> bookmarks = bookmarkRepository.findByUser(user, pageRequest);

		List<UserBookmarkResponse> userBookmarkResponses = bookmarks.stream()
			.map(this::getUserBookmarkResponse)
			.collect(Collectors.toList());

		return new SliceImpl<>(userBookmarkResponses, bookmarks.getPageable(), bookmarks.hasNext());
	}

	@Transactional(readOnly = true)
	public Slice<UserFeedResponse> getUserFeeds(User user, Pageable pageable) {

		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "visitedAt"));
		Slice<Feed> feeds = feedRepository.findByUser(user, pageRequest);

		List<UserFeedResponse> userFeedResponses = feeds.stream()
				.map(this::getUserFeedResponse)
				.collect(Collectors.toList());

		return new SliceImpl<>(userFeedResponses, feeds.getPageable(), feeds.hasNext());
	}

	private User findUserById(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));

		if (user.getDeletedAt() != null) {
			throw new CustomException(Result.DELETED_USER);
		}
		return user;
	}

	private Pageable getPageable(Pageable pageable, String sortBy) {
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, sortBy));
	}

	private UserBookmarkResponse getUserBookmarkResponse(Bookmark bookmark) {
		Store store = storeRepository.findById(bookmark.getBookmarkId())
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));
		User user = userRepository.findById(bookmark.getBookmarkId())
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));
		boolean isVisited = feedRepository.existsByStoreAndUser(store, user);

		return UserBookmarkResponse.of(
			bookmark.getBookmarkId(),
			store.getStoreId(),
			store.getStoreName(),
			store.getAddress(),
			store.getKakaoCategoryName(),
			isVisited
		);
	}

	private UserFeedResponse getUserFeedResponse(Feed feed) {
		Store store = storeRepository.findById(feed.getFeedId())
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

		return UserFeedResponse.of(
			feed.getFeedId(),
			store.getStoreId(),
			store.getStoreName(),
			feed.getCreatedAt(),
			store.getKakaoCategoryName(),
			feed.getRating(),
			feed.getImageUrl(),
			feed.getDescription()
		);
	}

	@Transactional
	public void deleteUser(User user, HttpServletResponse response) {
		userRepository.deleteById(user.getUserId());
		redisService.deleteValues(String.valueOf(user.getUserId()));
	}
}
