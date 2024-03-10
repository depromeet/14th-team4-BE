package com.depromeet.domains.bookmark.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.domains.bookmark.entity.Bookmark;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.user.entity.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom{
	Optional<Bookmark> findByUserAndStore(User user, Store store);
}
