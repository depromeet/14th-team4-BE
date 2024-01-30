package com.depromeet.domains.user.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.auth.service.RedisService;
import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.bookmark.entity.Bookmark;
import com.depromeet.domains.bookmark.repository.BookmarkRepository;
import com.depromeet.domains.review.entity.Review;
import com.depromeet.domains.review.repository.ReviewRepository;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.user.dto.response.UserBookmarkResponse;
import com.depromeet.domains.user.dto.response.UserProfileResponse;
import com.depromeet.domains.user.dto.response.UserReviewResponse;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
	private final RedisService redisService;
	private final UserRepository userRepository;
	private final BookmarkRepository bookmarkRepository;
	private final ReviewRepository reviewRepository;

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
		return UserProfileResponse.of(existUser.getNickName(), existUser.getLevel().getDescription());
	}

	@Transactional(readOnly = true)
	public Slice<UserBookmarkResponse> getUserBookmarks(User user, Pageable pageable) {
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
		Slice<Bookmark> bookmarks = bookmarkRepository.findByUser(user, pageRequest);

		List<UserBookmarkResponse> userBookmarkResponses = bookmarks.stream()
			.map(this::getUserBookemarkResponse)
			.collect(Collectors.toList());

		return new SliceImpl<>(userBookmarkResponses, bookmarks.getPageable(), bookmarks.hasNext());
	}

	@Transactional(readOnly = true)
	public Slice<UserReviewResponse> getUserReviews(User user, Pageable pageable) {

		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "visitedAt"));
		Slice<Review> reviews = reviewRepository.findByUser(user, pageRequest);

		List<UserReviewResponse> userReviewResponses = reviews.stream()
				.map(this::getUserReviewResponse)
				.collect(Collectors.toList());

		return new SliceImpl<>(userReviewResponses, reviews.getPageable(), reviews.hasNext());
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

	private UserBookmarkResponse getUserBookemarkResponse(Bookmark bookmark) {
		Store store = bookmark.getStore();
		boolean isVisited = reviewRepository.existsByStoreAndUser(store, bookmark.getUser());

		return UserBookmarkResponse.of(
			bookmark.getBookmarkId(),
			store.getStoreId(),
			store.getStoreName(),
			store.getAddress(),
			store.getStoreMeta().getTotalRevisitedCount(),
			store.getCategory().getCategoryName(),
			isVisited
		);
	}

	private UserReviewResponse getUserReviewResponse(Review review) {
		return UserReviewResponse.of(
			review.getReviewId(),
			review.getStore().getStoreId(),
			review.getStore().getStoreName(),
			review.getVisitTimes(),
			review.getVisitedAt(),
			review.getStore().getCategory().getCategoryName(),
			review.getRating(),
			review.getImageUrl(),
			review.getDescription()
		);
	}

	@Transactional
	public void deleteUser(User user) {
		userRepository.deleteById(user.getUserId());
		redisService.deleteValues(String.valueOf(user.getUserId()));
	}
}
