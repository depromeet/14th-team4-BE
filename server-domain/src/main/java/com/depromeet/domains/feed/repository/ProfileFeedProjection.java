package com.depromeet.domains.feed.repository;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileFeedProjection {

    private Long feedId;
    private Long userId;
    private Long storeId;
    private String storeName;
    private Long kakaoStoreId;
    private String address;
    private String feedImageUrl;
    private LocalDateTime feedCreatedAt;
    private Long likeCnt;
    private Long commentCnt;
    private Boolean isHeartFeed;

    @QueryProjection
    public ProfileFeedProjection(Long feedId, Long userId, Long storeId, String storeName, Long kakaoStoreId,
                       String address, String feedImageUrl, LocalDateTime feedCreatedAt,
                                 Long likeCnt, Long commentCnt, Boolean isHeartFeed) {
        this.feedId = feedId;
        this.userId = userId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.kakaoStoreId = kakaoStoreId;
        this.address = address;
        this.feedImageUrl = feedImageUrl;
        this.feedCreatedAt = feedCreatedAt;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
        this.isHeartFeed = isHeartFeed;
    }
}
