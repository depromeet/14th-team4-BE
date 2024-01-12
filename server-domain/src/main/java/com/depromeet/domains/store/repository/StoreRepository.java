package com.depromeet.domains.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.depromeet.domains.store.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	/**
	 * 특정 위, 경도 범위 안에 있는 식당 정보 + 해당 user의 북마크 여부
	 */
	@Query(value = ""
		+ "SELECT A.store_id "
		+ "		, A.store_name "
		+ "		, A.latitude "
		+ "		, A.longitude "
		+ "		, if(sum(isBookmark) = 1, true, false) AS isBookmarkedStore "
		+ "FROM ( "
		+ " 	SELECT s.store_id "
		+ "				, s.store_name "
		+ "			, IF(b.is_deleted = 0 AND b.user_id = :userId, 1, 0) AS isBookmark "
		+ "			, b.user_id "
		+ "			, s.latitude "
		+ "			, s.longitude "
		+ "		FROM Store s "
		+ "		LEFT JOIN Bookmark b ON s.store_id = b.store_id "
		+ "		WHERE s.latitude <= :maxLatitude "
		+ "		  AND s.latitude >= :minLatitude "
		+ "		  AND s.longitude <= :maxLongitude "
		+ "		  AND s.longitude >= :minLongitude "
		+ "		) A "
		+ "GROUP BY A.store_id ", nativeQuery = true)
	List<Object[]> findByLocationRangesWithIsBookmark(@Param("maxLatitude") Double maxLatitude
		, @Param("minLatitude") Double minLatitude
		, @Param("maxLongitude") Double maxLongitude
		, @Param("minLongitude") Double minLongitude
		, @Param("userId") Long userId
	);

	/**
	 * 각 식당들의 재방문한 사람들의 수
	 */
	@Query(value = ""
		+ "SELECT C.store_id, C.store_name, SUM(C.isMoreThanRevisit) AS numberOfRevisitedUser "
		+ "FROM ( "
		+ "		SELECT A.store_id, B.store_name, IF(B.review_cnt >= 2, 1, 0) AS isMoreThanRevisit "
		+ "		FROM Store A "
		+ "		INNER JOIN ( "
		+ "					SELECT s.store_id, s.store_name, r.user_id, COUNT(r.review_id) AS review_cnt "
		+ "					FROM Store s "
		+ "					LEFT JOIN Review r ON s.store_id = r.store_id "
		+ "					WHERE r.is_deleted = 0 "
		+ "						AND s.store_id IN :storeIdList "
		+ " 					GROUP BY s.store_id, r.user_id "
		+ "					) B ON A.store_id = B.store_id "
		+ "		) C "
		+ "GROUP BY C.store_id "
		+ "", nativeQuery = true)
	List<Object[]> findByStoresWithNumberOfRevisitedUser(@Param("storeIdList") List<Long> storeIdList);


	Boolean existsByStoreNameAndStoreAddress(String storeName, String storeAddress);


	Store findByKakaoStoreId(Long kakaoStoreId);
}
