package com.depromeet.domains.feed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FeedDetailResponse {
    private Long userId;
    private String profileImg;
    private String nickname;
    private Long storeId;
    private String storeName;
    private String kakaoCategoryName;
    private String address;
    private Long feedId;
    private String description;
    private String feedImg;
    private LocalDateTime createdAt; // 피드의 생성일자
    private Boolean isFollowed; // 팔로우 여부
    private Integer rating; // 별점

    /*
    private Integer commentCnt; // 댓글 수
    private Integer likeCnt; // 좋아요 수
    */
}
