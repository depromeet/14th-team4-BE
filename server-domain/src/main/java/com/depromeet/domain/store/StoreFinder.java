package com.depromeet.domain.store;

import java.util.List;

import org.springframework.stereotype.Component;

import com.depromeet.domain.store.entity.Store;
import com.depromeet.domain.store.persist.StorePersist;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreFinder {

	private final StorePersist storePersist;

	public List<Store> findAll() {
		return this.storePersist.findAll();
	}
}
