package com.depromeet.domains.store.repository;

import static com.depromeet.domains.store.entity.QStore.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.depromeet.domains.store.entity.Store;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class StoreQueryRepository {

	private final JPAQueryFactory queryFactory;

	// 테스트 (추후 지우기)
	public List<Store> getStoreAll() {
		return queryFactory
			.select(store)
			.from(store)
			.fetch();
	}

}
