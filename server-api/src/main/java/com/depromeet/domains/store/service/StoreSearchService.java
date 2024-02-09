package com.depromeet.domains.store.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.depromeet.domains.store.controller.KakaoSearchClient;
import com.depromeet.enums.CategoryType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.depromeet.domains.store.dto.response.StoreSearchResponse;
import com.depromeet.domains.store.dto.response.StoreSearchResult;
import com.depromeet.domains.store.entity.Store;
import com.depromeet.domains.store.repository.StoreRepository;
import com.depromeet.domains.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreSearchService {

	@Value("${kakao.rest-api}")
	private String kakaoRestApi;

	private final StoreRepository storeRepository;
	private final KakaoSearchClient kakaoSearchClient;

	@Transactional(readOnly = true)
	public StoreSearchResponse searchStoreList(String query, String x, String y, Optional<Integer> storePage,
		Optional<Integer> cafePage) {
		Integer size = 15;
		String sort = "distance";
		String authorization = "KakaoAK " + kakaoRestApi;

		List<StoreSearchResult> combinedResults = new ArrayList<>();

		Boolean storeIsEnd = true;
		if (storePage.isPresent()) {

			Map<String, Object> resultStore = kakaoSearchClient.searchKeyword(authorization, query, x, y, storePage.get(), size, sort, "FD6");
			List<Map<String, Object>> storeDocuments = (List<Map<String, Object>>) resultStore.get("documents");
			Map<String, Object> storeMeta = (Map<String, Object>) resultStore.get("meta");
			combinedResults.addAll(convertToStoreSearchResults(storeDocuments));
			storeIsEnd = (Boolean)storeMeta.get("is_end");
		}

		Boolean cafeIsEnd = true;
		if (cafePage.isPresent()) {
			Map<String, Object> resultStore = kakaoSearchClient.searchKeyword(authorization, query, x, y, cafePage.get(), size, sort, "CE7");
			List<Map<String, Object>> cafeDocuments = (List<Map<String, Object>>) resultStore.get("documents");
			Map<String, Object> cafeMeta = (Map<String, Object>) resultStore.get("meta");
			combinedResults.addAll(convertToStoreSearchResults(cafeDocuments));
			cafeIsEnd = (Boolean)cafeMeta.get("is_end");
		}

		log.info("search kakao");

		// 거리에 따라 정렬
		combinedResults.sort(Comparator.comparing(StoreSearchResult::getDistance));

		StoreSearchResponse responses = StoreSearchResponse.of(combinedResults, storeIsEnd, cafeIsEnd);

		return responses;

	}

	// 문서 목록을 StoreSearchResult 목록으로 변환하는 도우미 메서드
	private List<StoreSearchResult> convertToStoreSearchResults(List<Map<String, Object>> documents) {
		return documents.stream()
			.map(doc -> {
				Long storeId = null;
				Long kakaoStoreId = Long.parseLong(doc.get("id").toString());
				Integer distance = Integer.parseInt(doc.get("distance").toString());
				String storeName = doc.get("place_name").toString();
				String address = doc.get("address_name").toString();
				Long revisitedCount = 0L;
				String CategoryName = doc.get("category_name").toString();
				String[] kakaoCategories = CategoryName.split(" > ");
				String kakaoCategoryName ;
				if (kakaoCategories.length == 1){
					kakaoCategoryName = kakaoCategories[0];
				}else{
					kakaoCategoryName = kakaoCategories[1];
				}

				String categoryType = findByType(kakaoCategoryName);

				// storeName + address 조합후 DB 검색시 존재하지 않으면 새로운 음식점이므로 revisitedCount = 0,
				// 존재하면 revisitedCount = 해당 음식점의 revisitedCount
				log.info("db에서 revisitedCount 조회");
				if (storeRepository.existsByStoreNameAndAddress(storeName, address)) {
					Store store = storeRepository.findByKakaoStoreId(kakaoStoreId);
					storeId = store.getStoreId();
					log.info("store: {}", store);
					revisitedCount = store.getStoreMeta().getTotalRevisitedCount();
				}

				return StoreSearchResult.of(
					storeId,
					kakaoStoreId, // 카카오 스토어 ID
					storeName,
					kakaoCategoryName,
					categoryType,
					address,
					distance, // 숫자로 변환
					revisitedCount,
					Double.parseDouble(doc.get("x").toString()),
					Double.parseDouble(doc.get("y").toString()));
			})
			.collect(Collectors.toList());
	}

	private String findByType(String categoryName) {
		for (CategoryType categoryType : CategoryType.values()) {
			if (categoryType.getDescription().equals(categoryName)) {
				return categoryType.getDescription();
			}
		}
		return CategoryType.ETC.getDescription();
	}
}
