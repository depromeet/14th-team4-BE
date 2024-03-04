package com.depromeet.domains.store.repository;

import static com.depromeet.domains.store.entity.QStore.*;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import com.depromeet.domains.store.entity.Store;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;

	// 테스트 (추후 지우기)
	public List<Store> getStoreAll() {
		return jpaQueryFactory
			.select(store)
			.from(store)
			.fetch();
	}

}
