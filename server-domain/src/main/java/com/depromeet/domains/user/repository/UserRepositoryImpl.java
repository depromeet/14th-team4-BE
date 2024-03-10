package com.depromeet.domains.user.repository;

import org.springframework.stereotype.Repository;

import com.depromeet.domains.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByNickName(String nickname) {
        QUser user = QUser.user;
        Integer result = jpaQueryFactory
            .selectOne()
            .from(user)
            .where(user.nickName.eq(nickname))
            .fetchFirst();
        return result != null;
    }
}
