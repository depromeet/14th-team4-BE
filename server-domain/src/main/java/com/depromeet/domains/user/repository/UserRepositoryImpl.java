package com.depromeet.domains.user.repository;

import java.awt.print.Pageable;

import com.depromeet.domains.bookmark.entity.Bookmark;
import com.depromeet.domains.bookmark.entity.QBookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<Bookmark> findByUser(Long userId, Pageable pageable) {
        return null;
    }
}
