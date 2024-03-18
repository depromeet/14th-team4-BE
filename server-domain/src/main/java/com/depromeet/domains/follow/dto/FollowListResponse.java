package com.depromeet.domains.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowListResponse {
	private Long userId; // 팔로잉/팔로워의 userid
	private String nickname;
	private String profileImageUrl;
	private Boolean isFollowed; // 내가 이 user를 팔로우하고 있는지
}
