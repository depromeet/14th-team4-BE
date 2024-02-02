package com.depromeet.domains.user.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.user.dto.request.NicknameRequest;
import com.depromeet.domains.user.dto.response.UserBookmarkResponse;
import com.depromeet.domains.user.dto.response.UserProfileResponse;
import com.depromeet.domains.user.dto.response.UserReviewResponse;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	/**
	 * 유저 닉네임 수정
	 */
	@PutMapping("/nickname")
	public void updateUserNickname(@AuthUser User user, @RequestBody @Valid NicknameRequest nicknameRequest) {
		userService.updateUserNickname(user, nicknameRequest.getNickname());
	}

	/**
	 * 마이페이지 유저 닉네임, 등급 조회
	 */
	@GetMapping("/profile")
	public CustomResponseEntity<UserProfileResponse> getUserProfile(@AuthUser User user) {
		return CustomResponseEntity.success(userService.getUserProfile(user));
	}

	/**
	 * 내 북마크 반환
	 */
	@GetMapping("/bookmarks")
	public CustomResponseEntity<Slice<UserBookmarkResponse>> getMyBookmarks(@AuthUser User user, Pageable pageable) {
		return CustomResponseEntity.success(userService.getUserBookmarks(user, pageable));
	}

	/**
	 * 내 리뷰 반환
	 */
	@GetMapping("/reviews")
	public CustomResponseEntity<Slice<UserReviewResponse>> getMyReviews(@AuthUser User user, Pageable pageable) {
		return CustomResponseEntity.success(userService.getUserReviews(user, pageable));
	}

	@DeleteMapping("/withdraw")
	public void deleteUser(@AuthUser User user, HttpServletResponse response) {
		userService.deleteUser(user, response);
	}
}
