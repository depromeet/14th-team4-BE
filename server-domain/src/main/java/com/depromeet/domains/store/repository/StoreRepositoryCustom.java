package com.depromeet.domains.store.repository;

import java.util.List;

import com.depromeet.domains.store.entity.Store;

public interface StoreRepositoryCustom {

	List<Store> findByLocationRangesNotInBookmarks(double maxLatitude,
												double minLatitude,
												double maxLongitude,
												double minLongitude,
												List<Long> storeIdList);

	List<Store> findByStoreIdList(List<Long> storeIdList);
}
