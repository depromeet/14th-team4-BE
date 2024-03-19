package com.depromeet.domains.profile.service;

import com.depromeet.domains.follow.repository.FollowRepository;
import com.depromeet.domains.profile.dto.response.ProfileResponse;
import com.depromeet.domains.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@AllArgsConstructor
public class ProfileService {

    private final FollowRepository followRepository;

    public ProfileResponse getProfileInfo(User loginUser, Long profileUserId) {
        Long followingCnt = followRepository.getFollowingCountBySenderId(loginUser.getUserId());
        Long followerCnt = followRepository.getFollowerCountByReceiverId(loginUser.getUserId());
        boolean isMine = loginUser.getUserId().equals(profileUserId);
        boolean isFollowed = isUserFollowingMe(loginUser, profileUserId);

        return ProfileResponse.of(isMine, loginUser.getUserId(), loginUser.getProfileImageUrl()
                , loginUser.getNickName(), loginUser.getMyFeedCnt(), followerCnt, followingCnt, isFollowed);
    }

    private boolean isUserFollowingMe(User loginUser, Long profileUserId) {
        if (loginUser.getUserId().equals(profileUserId)) {
            return false;
        }

        return !ObjectUtils.isEmpty(followRepository.getFollowByEachId(profileUserId, loginUser.getUserId()));
    }
}
