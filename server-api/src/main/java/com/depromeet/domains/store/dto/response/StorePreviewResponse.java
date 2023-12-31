package com.depromeet.domains.store.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StorePreviewResponse {

    private Long storeId;
    private Long categoryId;
    private String storeName;
    private String address;
    private Float starRating;
    private Long reviewCount;
    private List<String> reviewImageUrls;
    private Long userId;
    private Long revisitedCount; // 자신이 재방문한 횟수(N번 방문)
    private Long totalRevisitedCount; // 전체 재방문 인원 수(00명이 재방문했어요)

    public static StorePreviewResponse of(Long storeId, Long categoryId, String storeName, String address, Float starRating, Long reviewCount, List<String> reviewImageUrls, Long userId, Long revisitedCount, Long totalRevisitedCount) {
        return StorePreviewResponse.builder()
                .storeId(storeId)
                .categoryId(categoryId)
                .storeName(storeName)
                .address(address)
                .starRating(starRating)
                .reviewCount(reviewCount)
                .reviewImageUrls(reviewImageUrls)
                .userId(userId)
                .revisitedCount(revisitedCount)
                .totalRevisitedCount(totalRevisitedCount)
                .build();
    }
}
