package com.depromeet.domains.profile.service;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.feed.repository.FeedRepository;
import com.depromeet.domains.follow.repository.FollowRepository;
import com.depromeet.domains.profile.dto.response.ProfileFeedResponse;
import com.depromeet.domains.profile.dto.response.ProfileResponse;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;
import com.depromeet.domains.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.depromeet.common.CursorPagingCommon.getSlice;

@Service
@AllArgsConstructor
public class ProfileService {

    private final FollowRepository followRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

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

    public Slice<ProfileFeedResponse> getProfileFeed(User loginUser, Long profileUserId, Long lastIdxId, Integer size) {
        User profileUser = userRepository.findById(profileUserId)
                .orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));

        List<ProfileFeedResponse> profileFeedList = feedRepository.findProfileFeed(loginUser, profileUser.getUserId(), lastIdxId, size)
                .stream()
                .map(ProfileFeedResponse::of)
                .collect(Collectors.toList());

        return getSlice(profileFeedList, size);
    }

    @Transactional
    public void updateUserNickname(User loginUser, Long profileUserId, String nickname) {
        validateIsSameAccount(loginUser, profileUserId);
        validateIsExistsNickname(nickname);
        User profileUser = userRepository.findById(profileUserId)
                .orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));
        profileUser.updateNickname(nickname);
    }

    private void validateIsSameAccount(User loginUser, Long profileUserId) {
        if (!loginUser.getUserId().equals(profileUserId)) {
            throw new CustomException(Result.CANNOT_MODIFY_INFORMATION_ANOTHER_ACCOUNT);
        }
    }

    private void validateIsExistsNickname(String nickname) {
        if (userRepository.existsByNickName(nickname)) {
            throw new CustomException(Result.DUPLICATED_NICKNAME);
        }
    }
}
