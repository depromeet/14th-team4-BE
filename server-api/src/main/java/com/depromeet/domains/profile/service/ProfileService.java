package com.depromeet.domains.profile.service;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.follow.repository.FollowRepository;
import com.depromeet.domains.profile.dto.response.ProfileResponse;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@AllArgsConstructor
public class ProfileService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public ProfileResponse getProfile(User loginUser, Long profileUserId) {
        Long followingCnt = followRepository.getFollowingCountBySenderId(loginUser.getUserId());
        Long followerCnt = followRepository.getFollowerCountByReceiverId(loginUser.getUserId());
        User profileUser = userRepository.findById(profileUserId)
                .orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));

        boolean isMine = loginUser.equals(profileUser);
        boolean isFollowed = isUserFollowingMe(loginUser, profileUserId);

        return ProfileResponse.of(isMine, profileUser.getUserId(), profileUser.getProfileImageUrl()
                , profileUser.getNickName(), profileUser.getMyFeedCnt(), followerCnt, followingCnt, isFollowed);
    }

    private boolean isUserFollowingMe(User loginUser, Long profileUserId) {
        if (loginUser.getUserId().equals(profileUserId)) {
            return false;
        }

        return !ObjectUtils.isEmpty(followRepository.getFollowByEachId(profileUserId, loginUser.getUserId()));
    }
}
