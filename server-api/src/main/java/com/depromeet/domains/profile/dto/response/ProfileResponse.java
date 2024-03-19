package com.depromeet.domains.profile.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {
    private boolean isMine;
    private long userId;
    private String profileImgUrl;
    private String nickname;
    private int feedcnt;
    private long follwerCnt;
    private long followingCnt;
    private boolean isFollowed;

    public static ProfileResponse of(boolean isMine, long userId, String profileImgUrl, String nickname
            , int feedCnt, long follwerCnt, long followingCnt, boolean isFollowed) {
        return ProfileResponse.builder()
                .isMine(isMine)
                .userId(userId)
                .profileImgUrl(profileImgUrl)
                .nickname(nickname)
                .feedcnt(feedCnt)
                .follwerCnt(follwerCnt)
                .followingCnt(followingCnt)
                .isFollowed(isFollowed)
                .build();
    }
}
