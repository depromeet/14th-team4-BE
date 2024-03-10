package com.depromeet.domains.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.depromeet.domains.store.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

	// todo - to queryDsl
	// @Query("SELECT s "
	// 	+ "      FROM Store s "
	// 	+ "      LEFT JOIN FETCH s.storeMeta sm "
	// 	+ "  WHERE s.location.latitude <= :maxLatitude "
	// 	+ " 	  AND s.location.latitude >= :minLatitude "
	// 	+ " 	  AND s.location.longitude <= :maxLongitude "
	// 	+ " 	  AND s.location.longitude >= :minLongitude "
	// 	+ " 	  AND (s.category.categoryType = :categoryType or :categoryType is null) "
	// 	+ " 	  AND s.storeId NOT IN (:storeIdList) "
	// 	+ "    ORDER BY sm.totalRevisitedCount DESC ")
	// List<Store> findByLocationRangesWithCategory(
	// 	@Param("maxLatitude") double maxLatitude,
	// 	@Param("minLatitude") double minLatitude,
	// 	@Param("maxLongitude") double maxLongitude,
	// 	@Param("minLongitude") double minLongitude,
	// 	@Param("categoryType") CategoryType categoryType,
	// 	@Param("storeIdList") List<Long> storeIdList);

	// todo - to queryDsl
	// @Query("SELECT s "
	// 	+ "      FROM Store s "
	// 	+ "      LEFT JOIN FETCH s.storeMeta sm "
	// 	+ "  WHERE s.location.latitude <= :maxLatitude "
	// 	+ " 	  AND s.location.latitude >= :minLatitude "
	// 	+ " 	  AND s.location.longitude <= :maxLongitude "
	// 	+ " 	  AND s.location.longitude >= :minLongitude "
	// 	+ " 	  AND (s.category.categoryType = :categoryType or :categoryType is null) "
	// 	+ "    ORDER BY sm.totalRevisitedCount DESC ")
	// List<Store> findByLocationRangesWithCategoryNoExcept(
	// 	@Param("maxLatitude") double maxLatitude,
	// 	@Param("minLatitude") double minLatitude,
	// 	@Param("maxLongitude") double maxLongitude,
	// 	@Param("minLongitude") double minLongitude,
	// 	@Param("categoryType") CategoryType categoryType);

	// @Query(" SELECT s "
	// 	+ "      FROM Bookmark bm "
	// 	+ "      LEFT JOIN bm.store s "
	// 	+ "    WHERE bm.user.userId = :userId ")
	// List<Store> findByUsersBookMarkList(@Param("userId") Long userId);

	Boolean existsByStoreNameAndAddress(String storeName, String address);

	Store findByKakaoStoreId(Long kakaoStoreId);

}
