package com.depromeet.domains.bookmark.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.domains.bookmark.entity.Bookmark;
import com.depromeet.domains.user.entity.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	Slice<Bookmark> findByUser(User user, Pageable pageable);
}
