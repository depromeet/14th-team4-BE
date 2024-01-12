package com.depromeet.domains.store.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StoreSearchResponse {

    private List<StoreSearchResult> storeSearchResult;
    private Boolean storeIsEnd;
    private Boolean cafeIsEnd;

    public static StoreSearchResponse of(List<StoreSearchResult> storeSearchResult, Boolean storeIsEnd, Boolean cafeIsEnd) {
        return StoreSearchResponse.builder()
                .storeSearchResult(storeSearchResult)
                .storeIsEnd(storeIsEnd)
                .cafeIsEnd(cafeIsEnd)
                .build();
    }
}
