package com.depromeet.domains.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsResponse {

    private Long userId;
    private String userProfile;
    private String nickname;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
