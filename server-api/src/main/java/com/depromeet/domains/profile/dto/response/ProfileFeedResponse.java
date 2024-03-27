package com.depromeet.domains.profile.dto.response;

import com.depromeet.domains.feed.repository.ProfileFeedProjection;
import lombok.*;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileFeedResponse {

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

    @Builder
    public ProfileFeedResponse(Long feedId, Long userId, Long storeId, String storeName, Long kakaoStoreId,
                               String address, String feedImageUrl, LocalDateTime feedCreatedAt, Long likeCnt
            , Long commentCnt, Boolean isHeartFeed) {
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

    public static ProfileFeedResponse of(Long feedId, Long userId, Long storeId, String storeName, Long kakaoStoreId, String address
            , String feedImageUrl, LocalDateTime feedCreatedAt, Long likeCnt, Long commentCnt, Boolean isHeartFeed) {
        return new ProfileFeedResponse(feedId, userId, storeId, storeName, kakaoStoreId, address
                , feedImageUrl, feedCreatedAt, likeCnt, commentCnt, isHeartFeed);
    }

    public static ProfileFeedResponse of(ProfileFeedProjection projection) {
        return ProfileFeedResponse.builder()
                .feedId(projection.getFeedId())
                .userId(projection.getUserId())
                .storeId(projection.getStoreId())
                .storeName(projection.getStoreName())
                .kakaoStoreId(projection.getKakaoStoreId())
                .address(splitStoreAddress(projection.getAddress()))
                .feedImageUrl(projection.getFeedImageUrl())
                .feedCreatedAt(projection.getFeedCreatedAt())
                .likeCnt(projection.getLikeCnt())
                .commentCnt(projection.getCommentCnt())
                .isHeartFeed(projection.getIsHeartFeed())
                .build();
    }

    private static String splitStoreAddress(String address) {
        if (!StringUtils.hasText(address)) {
            return "";
        }
        return Arrays.stream((address.split(" ")))
                .limit(2)
                .collect(Collectors.joining(" "));
    }
}
