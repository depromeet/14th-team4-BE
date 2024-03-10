package com.depromeet.domains.feed.service;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.feed.entity.Feed;
import com.depromeet.domains.feed.repository.FeedRepository;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.store.repository.StoreRepository;
import com.depromeet.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {

    private final FeedRepository feedRepository;
    private final StoreRepository storeRepository;

    // 피드 전체 조회

    // 피드 상세 조회

    // 피드 작성

    // 피드 수정

    // 피드 삭제
    @Transactional
    public void deleteFeed(User user, Long feedId) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_FEED));
        Store store = storeRepository.findById(feed.getStoreId()).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

        // 음식점 별점 감소, 사용자 피드 수 감소
        store.decreaseTotalRating(feed.getRating());
        user.decreaseMyFeedCount();

        feedRepository.delete(feed);
    }
}
