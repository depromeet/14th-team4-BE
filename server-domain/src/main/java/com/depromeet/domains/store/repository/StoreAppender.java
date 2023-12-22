package com.depromeet.domains.store.repository;

import com.depromeet.domains.store.entity.Store;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreAppender {
	private final StorePersist storePersist;

	public Long append(String storeName) {
		Store store = Store
			.builder()
			.storeName(storeName)
			.build();
		return storePersist.append(store);
	}

}
