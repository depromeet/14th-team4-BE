package com.depromeet.domains.feed.controller;

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.feed.dto.response.FeedDetailResponse;
import com.depromeet.domains.feed.dto.response.FeedResponse;
import com.depromeet.domains.feed.service.FeedService;
import com.depromeet.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1/feeds")
@RestController
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    // 피드 전체 조회
    @GetMapping
    public CustomResponseEntity<Slice<FeedResponse>> getFeeds(@AuthUser User user,
                                                              @RequestParam(value = "type") String type,
                                                              @RequestParam(value = "lastIdxId") Long lastIdxId,
                                                              @RequestParam(value = "size") Integer size) {
        return CustomResponseEntity.success(feedService.getFeeds(lastIdxId, user, type, size));
    }

    // 피드 상세 조회
    @GetMapping("/{feedId}")
    public CustomResponseEntity<FeedDetailResponse> getFeed(@AuthUser User user,
                                                            @PathVariable Long feedId) {
        return CustomResponseEntity.success(feedService.getFeed(user, feedId));
    }
    // 피드 작성

    // 피드 수정

    // 피드 삭제
    @DeleteMapping("/{feedId}")
    public CustomResponseEntity<Void> deleteFeed(@AuthUser User user,
                                                        @PathVariable Long feedId) {
        feedService.deleteFeed(user, feedId);
        return CustomResponseEntity.success();
    }
}
