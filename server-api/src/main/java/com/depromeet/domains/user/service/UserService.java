package com.depromeet.domains.user.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.bookmark.entity.Bookmark;
import com.depromeet.domains.bookmark.repository.BookmarkRepository;
import com.depromeet.domains.review.entity.Review;
import com.depromeet.domains.review.repository.ReviewRepository;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.user.dto.response.UserBookmarkResponse;
import com.depromeet.domains.user.dto.response.UserReviewResponse;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final BookmarkRepository bookmarkRepository;
	private final ReviewRepository reviewRepository;

	@Transactional
	public void updateUserNickname(Long userId, String nickname) {
		User user = findUserById(userId);
		user.updateNickname(nickname);
	}

	@Transactional(readOnly = true)
	public String getUserProfile(Long userId) {
		User user = findUserById(userId);
		return user.getNickName();
	}

	@Transactional(readOnly = true)
	public Slice<UserBookmarkResponse> getUserBookmarks(User user, Pageable pageable) {
		Slice<Bookmark> bookmarks = bookmarkRepository.findByUser(user, getPageable(pageable, "createdAt"));

		return bookmarks.map(this::getUserBookemarkResponse);
	}

	@Transactional(readOnly = true)
	public Slice<UserReviewResponse> getUserReviews(User user, Pageable pageable) {
		Slice<Review> reviews = reviewRepository.findByUser(user, getPageable(pageable, "visitedAt"));

		return reviews.map(this::getUserReviewResponse);
	}

	private User findUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));
	}

	private Pageable getPageable(Pageable pageable, String sortBy) {
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, sortBy));
	}

	private UserBookmarkResponse getUserBookemarkResponse(Bookmark bookmark) {
		Store store = bookmark.getStore();
		boolean isVisited = reviewRepository.existsByStoreAndUser(store, bookmark.getUser());
		Long totalRevisitedCount = reviewRepository.countTotalRevisitedCount(store);

		return UserBookmarkResponse.of(
			store.getStoreId(),
			store.getStoreName(),
			store.getRoadAddress(),
			totalRevisitedCount,
			store.getCategory().getCategoryName(),
			isVisited
		);
	}

	private UserReviewResponse getUserReviewResponse(Review review) {
		return UserReviewResponse.of(
			review.getStore().getStoreId(),
			review.getStore().getStoreName(),
			review.getVisitTimes(),
			review.getStore().getCategory().getCategoryName(),
			review.getRating(),
			review.getImageUrl(),
			review.getDescription()
		);
	}
}
