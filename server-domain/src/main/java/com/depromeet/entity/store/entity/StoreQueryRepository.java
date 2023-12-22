package com.depromeet.entity.store.entity;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class StoreQueryRepository {
	// queryDsl

	private final JPAQueryFactory queryFactory;

	public StoreQueryRepository(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}
}
