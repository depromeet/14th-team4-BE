package com.depromeet.domains.bookmark.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkingResponse {

    private Long bookmarkId;
    private Long userId;

    public static BookmarkingResponse of(Long bookmarkId, Long userId) {
        return BookmarkingResponse.builder()
                .bookmarkId(bookmarkId)
                .userId(userId)
                .build();
    }
}
