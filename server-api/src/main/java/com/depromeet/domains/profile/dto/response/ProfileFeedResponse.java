package com.depromeet.domains.profile.dto.response;

import com.depromeet.domains.feed.repository.ProfileFeedProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileFeedResponse {

    private Long feedId;
    private Long userId;
    private Long storeId;
    private String storeName;
    private Long kakaoStoreId;
    // todo - 위치(행정구)
    private String feedImageUrl;
    private LocalDateTime feedCreatedAt;
    private Long likeCnt;
    private Long commentCnt;
    private Boolean isHeartFeed;

    @Builder
    public ProfileFeedResponse(Long feedId, Long userId, Long storeId, String storeName, Long kakaoStoreId
            , String feedImageUrl, LocalDateTime feedCreatedAt, Long likeCnt, Long commentCnt, Boolean isHeartFeed) {
        this.feedId = feedId;
        this.userId = userId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.kakaoStoreId = kakaoStoreId;
        this.feedImageUrl = feedImageUrl;
        this.feedCreatedAt = feedCreatedAt;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
        this.isHeartFeed = isHeartFeed;
    }

    public static ProfileFeedResponse of(ProfileFeedProjection projection) {
        return ProfileFeedResponse.builder()
                .feedId(projection.getFeedId())
                .userId(projection.getUserId())
                .storeId(projection.getStoreId())
                .storeName(projection.getStoreName())
                .kakaoStoreId(projection.getKakaoStoreId())
                .feedImageUrl(projection.getFeedImageUrl())
                .feedCreatedAt(projection.getFeedCreatedAt())
                .likeCnt(projection.getLikeCnt())
                .commentCnt(projection.getCommentCnt())
                .isHeartFeed(projection.getIsHeartFeed())
                .build();


    }

}
