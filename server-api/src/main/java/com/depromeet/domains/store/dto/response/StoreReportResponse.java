package com.depromeet.domains.store.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreReportResponse {

    private Long storeId;
    private String storeMainImageUrl;
    private Long mostVisitedCount;
    private Long totalRevisitedCount;

    public static StoreReportResponse of(Long storeId, String storeMainImageUrl, Long mostVisitedCount, Long totalRevisitedCount) {
        return StoreReportResponse.builder()
                .storeId(storeId)
                .storeMainImageUrl(storeMainImageUrl)
                .mostVisitedCount(mostVisitedCount)
                .totalRevisitedCount(totalRevisitedCount)
                .build();
    }
}
