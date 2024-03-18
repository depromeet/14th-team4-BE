package com.depromeet.domains.profile.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {
    private boolean isMine;
    private String userId;
    private String profileImgUrl;
    private String nickname;
    private int feedcnt;
    private int follwerCnt;
    private int followingCnt;
    private boolean isFllowed;

    public static ProfileResponse of(boolean isMine, String userId, String profileImgUrl, String nickname
            , int feedCnt, int follwerCnt, int followingCnt, boolean isFllowed) {
        return ProfileResponse.builder()
                .isMine(isMine)
                .userId(userId)
                .profileImgUrl(profileImgUrl)
                .nickname(nickname)
                .feedcnt(feedCnt)
                .follwerCnt(follwerCnt)
                .followingCnt(followingCnt)
                .isFllowed(isFllowed)
                .build();
    }
}
