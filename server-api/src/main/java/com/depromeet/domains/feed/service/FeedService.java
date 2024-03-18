package com.depromeet.domains.feed.service;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.feed.dto.response.FeedDetailResponse;
import com.depromeet.domains.feed.dto.response.FeedResponse;
import com.depromeet.domains.feed.entity.Feed;
import com.depromeet.domains.feed.repository.FeedRepository;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.store.repository.StoreRepository;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.depromeet.common.CursorPagingCommon.getSlice;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {

    private final FeedRepository feedRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    // 피드 전체 조회
    @Transactional(readOnly = true)
    public Slice<FeedResponse> getFeeds(Long lastIdxId, User user, String type, Integer size) {
        User findUser = findUserById(user.getUserId());
        List<FeedResponse> feedResponses = feedRepository.findByUserIdAndType(lastIdxId, findUser.getUserId(), type, size);
        Slice<FeedResponse> responses= getSlice(feedResponses, size);
        return responses;
    }

    // 피드 상세 조회
    @Transactional(readOnly = true)
    public FeedDetailResponse getFeed(User user, Long feedId) {
        User findUser = findUserById(user.getUserId());
        FeedDetailResponse feedDetail = feedRepository.findFeedDetail(findUser.getUserId(), feedId);
        return feedDetail;
    }

    // 피드 작성

    // 피드 수정

    // 피드 삭제
    @Transactional
    public void deleteFeed(User user, Long feedId) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_FEED));
        Store store = storeRepository.findById(feed.getStoreId()).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

        // 음식점 별점 감소, 사용자 피드 수 감소
        store.decreaseTotalRating(feed.getRating());
        store.decreaseTotalFeedCnt();
        user.decreaseMyFeedCount();

        feedRepository.delete(feed);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));
    }
}
