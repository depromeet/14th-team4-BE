package com.depromeet.domains.store.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.depromeet.domains.review.repository.ReviewRepository;
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
	private final ReviewRepository reviewRepository;

	@Transactional(readOnly = true)
	public StoreSearchResponse searchStoreList(User user, String query, String x, String y, Optional<Integer> storePage,
		Optional<Integer> cafePage) {
		Integer size = 15;
		String sort = "distance";

		// 헤더 설정
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Authorization", "KakaoAK " + kakaoRestApi);

		List<Map<String, Object>> storeDocuments = new ArrayList<>();
		Boolean storeIsEnd = true;
		if (storePage.isPresent()) {
			Integer realStorePage = storePage.get();
			// 음식점 검색 API URI 생성
			UriComponentsBuilder builderStore = UriComponentsBuilder.fromHttpUrl(
					"https://dapi.kakao.com/v2/local/search/keyword.json")
				.queryParam("category_group_code", "FD6")
				.queryParam("x", x)
				.queryParam("y", y)
				.queryParam("page", realStorePage)
				.queryParam("size", size)
				.queryParam("sort", sort);

			HttpEntity<String> entityStore = new HttpEntity<>(httpHeaders);
			String uriStore = builderStore.build().encode().toUriString() + "&query=" + query; //이렇게 처리하지 않으면 결과가 안나옴
			ResponseEntity<Map> resultStore = restTemplate.exchange(uriStore, HttpMethod.GET, entityStore, Map.class);
			Map<String, Object> responseBodyStore = resultStore.getBody();
			storeDocuments = (List<Map<String, Object>>)responseBodyStore.get("documents");

			Map<String, Object> storeMeta = (Map<String, Object>)resultStore.getBody().get("meta");
			storeIsEnd = (Boolean)storeMeta.get("is_end");
		}

		List<Map<String, Object>> cafeDocuments = new ArrayList<>();
		Boolean cafeIsEnd = true;
		if (cafePage.isPresent()) {
			Integer realCafePage = cafePage.get();
			// 카페 검색 API URI 생성
			UriComponentsBuilder builderCafe = UriComponentsBuilder.fromHttpUrl(
					"https://dapi.kakao.com/v2/local/search/keyword.json")
				.queryParam("category_group_code", "CE7")
				.queryParam("x", x)
				.queryParam("y", y)
				.queryParam("page", realCafePage)
				.queryParam("size", size)
				.queryParam("sort", sort);

			HttpEntity<String> entityCafe = new HttpEntity<>(httpHeaders);
			String uriCafe = builderCafe.build().encode().toUriString() + "&query=" + query;
			ResponseEntity<Map> resultCafe = restTemplate.exchange(uriCafe, HttpMethod.GET, entityCafe, Map.class);
			Map<String, Object> responseBodyCafe = resultCafe.getBody();
			cafeDocuments = (List<Map<String, Object>>)responseBodyCafe.get("documents");

			Map<String, Object> cafeMeta = (Map<String, Object>)resultCafe.getBody().get("meta");
			cafeIsEnd = (Boolean)cafeMeta.get("is_end");
		}

		log.info("search kakao");

		// `resultStore` 및 `resultCafe`가 가게 및 카페에 대한 JSON 응답이라고 가정
		List<StoreSearchResult> storeResults = convertToStoreSearchResults(storeDocuments);
		List<StoreSearchResult> cafeResults = convertToStoreSearchResults(cafeDocuments);

		List<StoreSearchResult> combinedResults = new ArrayList<>();
		combinedResults.addAll(storeResults);
		combinedResults.addAll(cafeResults);

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
				String categoryName = doc.get("category_name").toString();
				String[] categories = categoryName.split(" > ");
				String category ;
				if (categories.length == 1){
					category = categories[0];
				}else{
					category = categories[1];
				}

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
					category,
					address,
					distance, // 숫자로 변환
					revisitedCount,
					Double.parseDouble(doc.get("x").toString()),
					Double.parseDouble(doc.get("y").toString()));
			})
			.collect(Collectors.toList());
	}
}
