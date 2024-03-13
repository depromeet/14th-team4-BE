package com.depromeet.domains.bookmark.repository;

import java.util.List;

import com.depromeet.domains.bookmark.entity.Bookmark;
import com.depromeet.domains.bookmark.entity.QBookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<Bookmark> findByUserId(Long userId, Pageable pageable) {
        QBookmark bookmark = QBookmark.bookmark;
        List<Bookmark> results = jpaQueryFactory.selectFrom(bookmark)
            .where(bookmark.userId.eq(userId))
            .orderBy(bookmark.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1) // 다음 페이지가 있는지 확인하기 위해 1개 더 가져옴
            .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        List<Bookmark> content = hasNext ? results.subList(0, pageable.getPageSize()) : results;

        return new SliceImpl<>(content, pageable, hasNext);
    }
}
