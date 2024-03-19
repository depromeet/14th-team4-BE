package com.depromeet.domains.follow.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowUpdateResponse {

	private Long followId;
	private Long senderId; // 내 아이디 (팔로우 요청하는 아이디)
	private Long receiverId; // 상대방 아이디 (팔로우 당하는 아이디)
	private Boolean isFollow;

	public static FollowUpdateResponse of(Long followId, Long senderId, Long receiverId, Boolean isFollow) {
		return FollowUpdateResponse.builder()
				.followId(followId)
				.senderId(senderId)
				.receiverId(receiverId)
				.isFollow(isFollow)
				.build();
	}
}
