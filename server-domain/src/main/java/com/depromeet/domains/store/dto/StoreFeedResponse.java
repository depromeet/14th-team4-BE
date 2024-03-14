package com.depromeet.domains.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@Getter
@Builder
@AllArgsConstructor
public class StoreFeedResponse {

    private Long userId;
    private Long feedId;
    private String profileImageUrl;
    private String nickName;
    private Integer rating;
    private String feedImageUrl;
    private LocalDate createdAt;
    private String description;
	private Boolean isMine;

    public StoreFeedResponse of(Long userId, Long feedId, String profileImageUrl, String nickName, Integer rating, String feedImageUrl, LocalDate createdAt, String description, Boolean isMine) {
    	return StoreFeedResponse.builder()
    			.userId(userId)
    			.feedId(feedId)
    			.profileImageUrl(profileImageUrl)
    			.nickName(nickName)
    			.rating(rating)
    			.feedImageUrl(feedImageUrl)
    			.createdAt(createdAt)
    			.description(description)
    			.isMine(isMine)
    			.build();
    }

}
