package com.depromeet.domains.store.repository;

import java.util.List;

import com.depromeet.domains.store.entity.Store;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreFinder {

	private final StorePersist storePersist;

	public List<Store> findAll() {
		return this.storePersist.findAll();
	}
}
