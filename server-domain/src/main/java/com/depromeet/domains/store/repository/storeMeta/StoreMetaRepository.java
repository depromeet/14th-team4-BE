package com.depromeet.domains.store.repository.storeMeta;

import com.depromeet.domains.store.entity.StoreMeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreMetaRepository extends JpaRepository<StoreMeta, Long>, StoreMetaRepositoryCustom {
}
