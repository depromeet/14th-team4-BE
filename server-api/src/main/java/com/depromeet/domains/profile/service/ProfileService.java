package com.depromeet.domains.profile.service;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.feed.repository.FeedRepository;
import com.depromeet.domains.feed.repository.ProfileFeedProjection;
import com.depromeet.domains.follow.repository.FollowRepository;
import com.depromeet.domains.profile.dto.response.ProfileResponse;
import com.depromeet.domains.store.dto.StoreFeedResponse;
import com.depromeet.domains.store.repository.StoreRepository;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.depromeet.common.CursorPagingCommon.getSlice;

@Service
@AllArgsConstructor
public class ProfileService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final StoreRepository storeRepository;

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

    public Slice<ProfileResponse> getProfileFeed(User loginUser, Long profileUserId, Long lastIdxId, Integer size) {
        User profileUser = userRepository.findById(profileUserId)
                .orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));

        List<ProfileFeedProjection> profileFeedList
                            = feedRepository.findProfileFeed(loginUser, profileUser.getUserId(), lastIdxId, size);
        return getSlice(profileFeedList, size);
    }
}
