package com.depromeet.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import com.depromeet.document.RestDocsTestSupport;
import com.depromeet.domains.store.dto.response.StoreSearchResponse;
import com.depromeet.domains.store.dto.response.StoreSearchResult;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser
public class StoreSearchControllerTest extends RestDocsTestSupport {

	@Test
	@DisplayName("음식점, 카페 통합 검색")
	void search() throws Exception {
		// given
		String query = "스타";
		String x = "127.0628310";
		String y = "37.51432257";
		Integer storePage = 1;
		Integer cafePage = 1;

		StoreSearchResult storeSearchResult1 = StoreSearchResult.builder()
			.storeId(1L)
			.storeName("티컵 스타필드 코엑스몰점")
			.kakaoCategoryName("카페")
			.categoryType("CAFE")
			.address("서울 강남구 삼성동 159")
			.distance(389)
			.totalRevisitedCount(3L)
			.latitude(127.058938708812)
			.longitude(37.5126847515106)
			.kakaoStoreId(1234567L)
			.build();

		StoreSearchResult storeSearchResult2 = StoreSearchResult.builder()
			.storeId(2L)
			.storeName("스타가든")
			.kakaoCategoryName("간식")
			.categoryType("ETC")
			.address("서울 강남구 역삼동 737")
			.distance(2810)
			.totalRevisitedCount(0L)
			.latitude(127.036568685902)
			.longitude(37.5000544957843)
			.kakaoStoreId(1112233L)
			.build();

		List<StoreSearchResult> storeSearchResultList = List.of(storeSearchResult1, storeSearchResult2);

		Boolean storeIsEnd = false;
		Boolean cafeIsEnd = false;

		StoreSearchResponse storeSearchResponse = StoreSearchResponse.builder()
			.storeSearchResult(storeSearchResultList)
			.storeIsEnd(storeIsEnd)
			.cafeIsEnd(cafeIsEnd)
			.build();

		given(storeSearchService.searchStoreList(eq(query), eq(x), eq(y),
			eq(java.util.Optional.ofNullable(storePage)), eq(java.util.Optional.ofNullable(cafePage)))).willReturn(
			storeSearchResponse);

		// when
		mockMvc.perform(
				get("/api/v1/stores/search")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken")
					.param("query", query)
					.param("x", x)
					.param("y", y)
					.param("storePage", String.valueOf(storePage))
					.param("cafePage", String.valueOf(cafePage)))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					queryParameters(
						parameterWithName("query").description("검색 키워드"),
						parameterWithName("x").description("경도"),
						parameterWithName("y").description("위도"),
						parameterWithName("storePage").description("음식점 페이지 - optional").optional(),
						parameterWithName("cafePage").description("카페 페이지 - optional").optional()
					),
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						fieldWithPath("data.storeSearchResult[]").type(JsonFieldType.ARRAY).description("리뷰 목록"),
						fieldWithPath("data.storeSearchResult[].storeId").type(JsonFieldType.NUMBER)
							.description("우리 DB상 음식점 ID"),
						fieldWithPath("data.storeSearchResult[].storeName").type(JsonFieldType.STRING)
							.description("음식점 명"),
						fieldWithPath("data.storeSearchResult[].kakaoCategoryName").type(JsonFieldType.STRING)
							.description("카카오 카테고리"),
						fieldWithPath("data.storeSearchResult[].categoryType").type(JsonFieldType.STRING)
							.description("카테고리 타입 - 또잇또잇 db 기준"),
						fieldWithPath("data.storeSearchResult[].address").type(JsonFieldType.STRING)
							.description("음식점 주소"),
						fieldWithPath("data.storeSearchResult[].distance").type(JsonFieldType.NUMBER)
							.description("현재 위치와 음식점과의 거리"),
						fieldWithPath("data.storeSearchResult[].totalRevisitedCount").type(JsonFieldType.NUMBER)
							.description("재방문한 인원수 (N명 재방문)"),
						fieldWithPath("data.storeSearchResult[].longitude").type(JsonFieldType.NUMBER)
							.description("음식점 경도"),
						fieldWithPath("data.storeSearchResult[].latitude").type(JsonFieldType.NUMBER)
							.description("음식점 위도"),
						fieldWithPath("data.storeSearchResult[].kakaoStoreId").type(JsonFieldType.NUMBER)
							.description("카카오 DB상 음식점 ID"),
						fieldWithPath("data.storeIsEnd").type(JsonFieldType.BOOLEAN)
							.description("음식점 검색 결과 마지막 페이지 여부"),
						fieldWithPath("data.cafeIsEnd").type(JsonFieldType.BOOLEAN).description("카페 검색 결과 마지막 페이지 여부")
					)
				)
			);
	}
}
