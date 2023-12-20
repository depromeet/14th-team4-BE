package com.depromeet.entity.store.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.depromeet.entity.store.StoreFinder;
import com.depromeet.entity.store.entity.Store;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreFinder storeFinder;

	public List<Store> findAll() {
		return this.storeFinder.findAll();
	}

}
