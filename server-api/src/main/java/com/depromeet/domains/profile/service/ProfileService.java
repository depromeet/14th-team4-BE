package com.depromeet.domains.profile.service;

import com.depromeet.domains.follow.repository.FollowRepository;
import com.depromeet.domains.profile.dto.response.ProfileResponse;
import com.depromeet.domains.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileService {

    private final FollowRepository followRepository;

    public ProfileResponse getProfileInfo(User user, boolean isMine) {
        Long followingCnt = followRepository.getFollowingCountBySenderId(user.getUserId());
        Long followerCnt = followRepository.getFollowerCountByReceiverId(user.getUserId());

        return ProfileResponse.of(isMine, user.getUserId(), user.getProfileImageUrl()
                , user.getNickName(), user.getMyFeedCnt(), followerCnt, followingCnt, false);
    }
}
