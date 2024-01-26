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

    @PatchMapping("/bookmarks/{storeId}")
    public CustomResponseEntity<BookmarkingResponse> updateBookmark( @AuthUser User user, @PathVariable Long storeId) {
        return CustomResponseEntity.success(bookmarkService.updateBookmark(user, storeId));
    }
}
