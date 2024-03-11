package com.depromeet.domains.feed.controller;

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.feed.service.FeedService;
import com.depromeet.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1/feeds")
@RestController
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    // 피드 전체 조회

    // 피드 상세 조회

    // 피드 작성

    // 피드 수정

    // 피드 삭제
    @DeleteMapping("/{feedId}")
    public CustomResponseEntity<Void> deleteStoreReview(@AuthUser User user,
                                                        @PathVariable Long feedId) {
        feedService.deleteFeed(user, feedId);
        return CustomResponseEntity.success();
    }
}
