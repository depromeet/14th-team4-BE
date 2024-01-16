package com.depromeet.domains.bookmark.controller;

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.bookmark.dto.response.BookmarkingResponse;
import com.depromeet.domains.bookmark.service.BookmarkService;
import com.depromeet.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/bookmarks/{storeId}")
    public CustomResponseEntity<BookmarkingResponse> createBookmark(@PathVariable Long storeId, @AuthUser User user) {
        return CustomResponseEntity.success(bookmarkService.createBookmark(storeId, user));
    }

    @DeleteMapping("/bookmarks/{bookmarkId}")
    public CustomResponseEntity<Void> deleteBookmark(@PathVariable Long bookmarkId, @AuthUser User user) {
        return CustomResponseEntity.success(bookmarkService.deleteBookmark(bookmarkId, user));
    }
}
