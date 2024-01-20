package com.depromeet.domains.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.depromeet.domains.store.entity.Store;
import com.depromeet.enums.CategoryType;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	@Query("SELECT s "
		+ "      FROM Store s "
		+ "      LEFT JOIN FETCH s.storeMeta sm "
		+ "  WHERE s.location.latitude <= :maxLatitude "
		+ " 	  AND s.location.latitude >= :minLatitude "
		+ " 	  AND s.location.longitude <= :maxLongitude "
		+ " 	  AND s.location.longitude >= :minLongitude "
		+ " 	  AND (s.category.categoryType = :categoryType or :categoryType is null) "
		+ " 	  AND s.storeId NOT IN (:storeIdList) "
		+ "    ORDER BY sm.totalReviewCount ")
	List<Store> findByLocationRangesWithCategory(
		@Param("maxLatitude") double maxLatitude,
		@Param("minLatitude") double minLatitude,
		@Param("maxLongitude") double maxLongitude,
		@Param("minLongitude") double minLongitude,
		@Param("categoryType") CategoryType categoryType,
		@Param("storeIdList") List<Long> storeIdList);

	@Query(" SELECT s "
		+ "      FROM Bookmark bm "
		+ "      LEFT JOIN bm.store s "
		+ "    WHERE bm.user.userId = :userId ")
	List<Store> findByUsersBookMarkList(@Param("userId") Long userId);

	Boolean existsByStoreNameAndAddress(String storeName, String address);

	Store findByKakaoStoreId(Long kakaoStoreId);

}
