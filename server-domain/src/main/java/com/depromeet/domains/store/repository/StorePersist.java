package com.depromeet.domains.store.repository;

import com.depromeet.domains.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StorePersist {

    private final StoreRepository storeRepository;
    private final StoreQueryRepository storeQueryRepository;

    public List<Store> findAll() {
        return this.storeRepository.findAll();
    }

    public Long append(Store store) {
        return this.storeRepository.save(store).getStoreId();
    }
}