package com.depromeet.domains.bookmark.service;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.bookmark.dto.response.BookmarkingResponse;
import com.depromeet.domains.bookmark.entity.Bookmark;
import com.depromeet.domains.bookmark.repository.BookmarkRepository;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.store.repository.StoreRepository;
import com.depromeet.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final StoreRepository storeRepository;

    public BookmarkingResponse updateBookmark(User user, Long storeId) {

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

        // 사용자의 현재 북마크 상태 확인
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserAndStore(user, store);

        if (existingBookmark.isPresent()) {
            // 이미 북마크된 음식점일 경우 북마크 제거
            bookmarkRepository.delete(existingBookmark.get());
            return bookmarkingResponse(existingBookmark.get().getBookmarkId(), user.getUserId());
        }
        // 새로운 북마크 추가
        Bookmark newBookmark = Bookmark.builder()
                .storeId(storeId)
                .userId(user.getUserId())
                .build();

        bookmarkRepository.save(newBookmark);
        return bookmarkingResponse(newBookmark.getBookmarkId(), user.getUserId());
    }

    private BookmarkingResponse bookmarkingResponse(Long bookmarkId, Long userId) {
        return BookmarkingResponse.builder()
                .bookmarkId(bookmarkId)
                .userId(userId)
                .build();
    }
}
