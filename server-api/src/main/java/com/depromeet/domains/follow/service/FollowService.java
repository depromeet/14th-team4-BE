package com.depromeet.domains.follow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.follow.dto.FollowUpdateResponse;
import com.depromeet.domains.follow.dto.FollowListResponse;
import com.depromeet.domains.follow.entity.Follow;
import com.depromeet.domains.follow.repository.FollowRepository;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;
import com.depromeet.enums.FollowType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

	private final FollowRepository followRepository;
	private final UserRepository userRepository;

	public FollowUpdateResponse updateFollow(User user, Long receiverId) {
		if (user.getUserId().equals(receiverId)) {
			throw new CustomException(Result.CANNOT_FOLLOW_YOURSELF);
		}
		User receiver = userRepository.findById(receiverId)
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));

		Optional<Follow> follow = followRepository.findBySenderIdAndReceiverId(user.getUserId(), receiver.getUserId());
		if (follow.isPresent()) {
			followRepository.delete(follow.get());
			return FollowUpdateResponse.of(follow.get().getFollowId(), user.getUserId(), receiver.getUserId(), false);
		}

		// 팔로우 관계가 없으면 새로 생성 (팔로우)
		Follow newFollow = Follow.builder()
			.senderId(user.getUserId())
			.receiverId(receiver.getUserId())
			.build();
		followRepository.save(newFollow);
		return FollowUpdateResponse.of(newFollow.getFollowId(), user.getUserId(), receiver.getUserId(), true);
	}

	public List<FollowListResponse> getFollowList(User user, Long userId, FollowType type) {
		return followRepository.findFollowList(user.getUserId(), userId, type);
	}
}
