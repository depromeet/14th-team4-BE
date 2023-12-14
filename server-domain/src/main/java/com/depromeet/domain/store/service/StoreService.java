package com.depromeet.domain.store.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.depromeet.domain.store.StoreFinder;
import com.depromeet.domain.store.entity.Store;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreFinder storeFinder;

	public List<Store> findAll() {
		return this.storeFinder.findAll();
	}

}
