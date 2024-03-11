package com.depromeet.domains.store.repository;

import static com.depromeet.domains.store.entity.QStore.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.depromeet.domains.store.entity.Store;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Store> findByLocationRangesNotInBookmarks(double maxLatitude, double minLatitude, double maxLongitude,
		double minLongitude, List<Long> storeIdList) {

		return jpaQueryFactory.select(store)
			.from(store)
			.where(searchLocationRangesWithBookmarks(
				maxLatitude, minLatitude, maxLongitude, minLongitude, storeIdList
			))
			.fetch();
	}

	@Override
	public List<Store> findByStoreIdList(List<Long> storeIdList) {
		return jpaQueryFactory.select(store)
			.from(store)
			.where(store.storeId.in(storeIdList))
			.fetch();
	}

	private BooleanExpression searchLocationRangesWithBookmarks(double maxLatitude, double minLatitude, double maxLongitude,
		double minLongitude, List<Long> storeIdList) {
		BooleanExpression condition = store.location.latitude.loe(maxLatitude)
			.and(store.location.latitude.goe(minLatitude))
			.and(store.location.longitude.loe(maxLongitude))
			.and(store.location.longitude.goe(minLongitude));

		if (storeIdList.size() == 0) {
			condition = condition.and(store.storeId.notIn(storeIdList));
		}
		return condition;
	}

}
