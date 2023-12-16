package com.depromeet.domain.store.persist;

import java.util.List;

import org.springframework.stereotype.Component;

import com.depromeet.domain.store.entity.Store;
import com.depromeet.domain.store.entity.StoreQueryRepository;
import com.depromeet.domain.store.entity.StoreRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StorePersist {

	private final StoreRepository storeRepository;
	private final StoreQueryRepository storeQueryRepository;

	public List<Store> findAll() {
		return this.storeRepository.findAll();
	}

	public Long append(Store store) {
		return this.storeRepository.save(store).getId();
	}
}
