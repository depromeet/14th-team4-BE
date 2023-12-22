package com.depromeet.entity.store;

import java.util.List;

import org.springframework.stereotype.Component;

import com.depromeet.entity.store.entity.Store;
import com.depromeet.entity.store.persist.StorePersist;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreFinder {

	private final StorePersist storePersist;

	public List<Store> findAll() {
		return this.storePersist.findAll();
	}
}
