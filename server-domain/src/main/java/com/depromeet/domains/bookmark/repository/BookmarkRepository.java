package com.depromeet.domains.bookmark.repository;

import com.depromeet.domains.store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.domains.bookmark.entity.Bookmark;
import com.depromeet.domains.user.entity.User;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom{
	Slice<Bookmark> findByUser(User user, Pageable pageable);

	Optional<Bookmark> findByUserAndStore(User user, Store store);
}
