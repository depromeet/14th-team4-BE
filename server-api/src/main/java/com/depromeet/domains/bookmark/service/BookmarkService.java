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

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final StoreRepository storeRepository;


    public BookmarkingResponse createBookmark(Long storeId, User user) {

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_STORE));

        Bookmark bookmark = Bookmark.builder()
                .store(store)
                .user(user)
                .build();

        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return BookmarkingResponse.of(savedBookmark.getBookmarkId(), user.getUserId());
    }

    public BookmarkingResponse deleteBookmark(Long bookmarkId, User user) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new CustomException(Result.NOT_FOUND_BOOKMARK));

        if (!bookmark.getUser().getUserId().equals(user.getUserId())) {
            throw new CustomException(Result.UNAUTHORIZED_USER);
        }

        bookmarkRepository.delete(bookmark);
        return BookmarkingResponse.of(bookmark.getBookmarkId(), user.getUserId());
    }
}
