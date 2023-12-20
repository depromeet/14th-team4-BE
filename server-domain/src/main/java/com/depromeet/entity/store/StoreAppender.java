package com.depromeet.entity.store;

import org.springframework.stereotype.Component;

import com.depromeet.entity.store.entity.Store;
import com.depromeet.entity.store.persist.StorePersist;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreAppender {
	private final StorePersist storePersist;

	public Long append(String storeName) {
		Store store = Store
			.builder()
			.name(storeName)
			.build();
		return storePersist.append(store);
	}

}
