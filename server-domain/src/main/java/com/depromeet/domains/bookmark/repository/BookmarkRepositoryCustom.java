package com.depromeet.domains.bookmark.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.depromeet.domains.bookmark.entity.Bookmark;

public interface BookmarkRepositoryCustom {
	Slice<Bookmark> findByUserId(Long userId, Pageable pageable);
}
