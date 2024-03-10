package com.depromeet.domains.user.repository;

import java.awt.print.Pageable;
import org.springframework.data.domain.Slice;
import com.depromeet.domains.bookmark.entity.Bookmark;

public interface UserRepositoryCustom {
	Slice<Bookmark> findByUser(Long userId, Pageable pageable);

}
