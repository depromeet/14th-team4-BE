package com.depromeet.domains.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.depromeet.domains.store.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

	Boolean existsByStoreNameAndAddress(String storeName, String address);

	Store findByKakaoStoreId(Long kakaoStoreId);

}
