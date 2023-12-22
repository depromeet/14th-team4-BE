package com.depromeet.service.store;

import com.depromeet.domains.store.repository.StoreFinder;
import com.depromeet.domains.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreFinder storeFinder;

	public List<Store> findAll() {
		return this.storeFinder.findAll();
	}

}
