package com.depromeet.domains.follow.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.follow.dto.FollowUpdateResponse;
import com.depromeet.domains.follow.dto.FollowListResponse;
import com.depromeet.domains.follow.service.FollowService;
import com.depromeet.domains.user.entity.User;

@RequestMapping("/api/v1/follows")
@RestController
@RequiredArgsConstructor
public class FollowController {
	private final FollowService followService;

	// 팔로잉 등록, 팔로잉 취소 (팔로잉할, 팔로잉 취소할 user id)
	@PatchMapping("/{receiverId}")
	public CustomResponseEntity<FollowUpdateResponse> updateFollow (@AuthUser User user, @PathVariable Long receiverId) {
		return CustomResponseEntity.success(followService.updateFollow(user, receiverId));
	}

	// 팔로우 목록 조회
	@GetMapping("/{userId}/followers")
	public CustomResponseEntity<List<FollowListResponse>> getFollowList(@AuthUser User user, @PathVariable Long userId) {
		return CustomResponseEntity.success(followService.getFollwerList(user, userId));
	}

	@GetMapping("/{userId}/followings")
	public CustomResponseEntity<List<FollowListResponse>> getFollowingList(@AuthUser User user, @PathVariable Long userId) {
		return CustomResponseEntity.success(followService.getFollowingList(user, userId));
	}
}
