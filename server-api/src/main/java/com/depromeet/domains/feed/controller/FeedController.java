package com.depromeet.domains.feed.controller;

import com.depromeet.domains.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/feeds")
@RestController
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
}
