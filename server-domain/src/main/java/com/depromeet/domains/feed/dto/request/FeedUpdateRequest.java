package com.depromeet.domains.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedUpdateRequest {
    private Integer rating;
    private String imageUrl;
    private String description;
}
