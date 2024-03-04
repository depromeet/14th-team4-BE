package com.depromeet.domains.store.repository.storeMeta;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreMetaRepositoryImpl implements StoreMetaRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
}
