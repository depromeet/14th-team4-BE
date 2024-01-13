package com.depromeet.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.depromeet.domains.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import com.depromeet.document.RestDocsTestSupport;
import com.depromeet.domains.store.dto.response.StorePreviewResponse;
import com.depromeet.domains.store.dto.response.StoreReportResponse;
import com.depromeet.domains.store.dto.response.StoreReviewResponse;
import com.depromeet.enums.ReviewType;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser
class StoreControllerTest extends RestDocsTestSupport {

	@Test
	void getStore() throws Exception {
		// given

		StorePreviewResponse storePreviewResponse = StorePreviewResponse.builder()
			.storeId(1L)
			.categoryName("중식")
			.storeName("칠기마라탕")
			.address("서울시 강남구 역삼동 123-123")
			.starRating(4.1F)
			.reviewCount(10L)
			.reviewImageUrls(List.of("https://image.com/1.jpg", "https://image.com/2.jpg"))
			.userId(1L)
			.revisitedCount(5L)
			.totalRevisitedCount(2L)
			.build();

		given(storeService.getStore(eq(1L), any())).willReturn(storePreviewResponse);

		// when
		mockMvc.perform(
				get("/api/v1/stores/{storeId}", 1L)
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken"))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					pathParameters(
						parameterWithName("storeId").description("음식점 ID")
					),
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						fieldWithPath("data.storeId").type(JsonFieldType.NUMBER).description("음식점 ID"),
						fieldWithPath("data.categoryName").type(JsonFieldType.STRING).description("카테고리 명"),
						fieldWithPath("data.storeName").type(JsonFieldType.STRING).description("음식점 명"),
						fieldWithPath("data.address").type(JsonFieldType.STRING).description("음식점 주소"),
						fieldWithPath("data.starRating").type(JsonFieldType.NUMBER).description("음식점 별점"),
						fieldWithPath("data.reviewCount").type(JsonFieldType.NUMBER).description("리뷰 개수"),
						fieldWithPath("data.reviewImageUrls").type(JsonFieldType.ARRAY).description("리뷰 이미지 URL"),
						fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
						fieldWithPath("data.revisitedCount").type(JsonFieldType.NUMBER)
							.description("자신이 재방문한 횟수(N번 방문)"),
						fieldWithPath("data.totalRevisitedCount").type(JsonFieldType.NUMBER)
							.description("전체 재방문 인원 수(00명이 재방문했어요)")
					)
				)
			)
		;
	}

	@Test
	void getStoreReport() throws Exception {
		// given
		StoreReportResponse storeReportResponse = StoreReportResponse.builder()
			.storeId(1L)
			.storeMainImageUrl("https://image.com/1.jpg")
			.mostVisitedCount(15L)
			.totalRevisitedCount(100L)
			.build();

		given(storeService.getStoreReport(eq(1L))).willReturn(storeReportResponse);

		// when
		mockMvc.perform(
				get("/api/v1/stores/{storeId}/reports", 1L)
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken"))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					pathParameters(
						parameterWithName("storeId").description("음식점 ID")
					),
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						fieldWithPath("data.storeId").type(JsonFieldType.NUMBER).description("음식점 ID"),
						fieldWithPath("data.storeMainImageUrl").type(JsonFieldType.STRING)
							.description("음식점 대표 이미지 URL"),
						fieldWithPath("data.mostVisitedCount").type(JsonFieldType.NUMBER)
							.description("가장 많이 유저의 방문한 횟수"),
						fieldWithPath("data.totalRevisitedCount").type(JsonFieldType.NUMBER).description("전체 재방문 인원 수")
					)
				)
			);
	}

	@Test
	void getStoreReview() throws Exception {
		// given
		StoreReviewResponse storeReviewResponse1 = StoreReviewResponse.builder()
			.userId(1L)
			.nickName("김철수")
			.rating(4F)
			.imageUrl("https://image.com/1.jpg")
			.visitTimes(3)
			.visitedAt(LocalDateTime.now())
			.description("맛있어요")
			.isMine(true)
			.build();

		StoreReviewResponse storeReviewResponse2 = StoreReviewResponse.builder()
			.userId(2L)
			.nickName("김길동")
			.rating(2F)
			.imageUrl("https://image.com/2.jpg")
			.visitTimes(1)
			.visitedAt(LocalDateTime.now())
			.description("맛있어요")
			.isMine(false)
			.build();

		StoreReviewResponse storeReviewResponse3 = StoreReviewResponse.builder()
			.userId(3L)
			.nickName("맛있는 음식을보면 짖는 개")
			.rating(3F)
			.imageUrl(null)
			.visitTimes(1)
			.visitedAt(LocalDateTime.now())
			.description("왈왈왈왈왈왈왈")
			.isMine(false)
			.build();

		ReviewType reviewType = ReviewType.REVISITED;

		List<StoreReviewResponse> content = Arrays.asList(storeReviewResponse1, storeReviewResponse2,
			storeReviewResponse3);
		Slice<StoreReviewResponse> storeReviewResponses = new SliceImpl<>(content, Pageable.unpaged(), true);

		given(storeService.getStoreReview(any(), eq(1L), eq(Optional.of(ReviewType.REVISITED)), any(Pageable.class))).willReturn(storeReviewResponses);

		// when
		mockMvc.perform(
				get("/api/v1/stores/{storeId}/reviews", 1L)
					//                                .with(csrf())
					.param("type", ReviewType.REVISITED.name())
					.param("page", "0")
					.param("size", "20")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken"))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					queryParameters(
						parameterWithName("page").description("페이지 번호 (1번 부터)"),
						parameterWithName("size").description("페이지 사이즈"),
						parameterWithName("type").description("리뷰 타입 - revisited, photo").optional()
					),
					pathParameters(
						parameterWithName("storeId").description("음식점 ID")
					),
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						subsectionWithPath("data.content[]").type(JsonFieldType.ARRAY).description("리뷰 목록"),
						fieldWithPath("data.content[].userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
						fieldWithPath("data.content[].nickName").type(JsonFieldType.STRING).description("닉네임"),
						fieldWithPath("data.content[].rating").type(JsonFieldType.NUMBER).description("평점"),
						fieldWithPath("data.content[].imageUrl").type(JsonFieldType.STRING)
							.description("이미지 URL")
							.optional(),
						fieldWithPath("data.content[].visitTimes").type(JsonFieldType.NUMBER).description("방문 횟수"),
						fieldWithPath("data.content[].visitedAt").type(JsonFieldType.STRING).description("방문 일시"),
						fieldWithPath("data.content[].description").type(JsonFieldType.STRING).description("리뷰 내용"),
						fieldWithPath("data.content[].isMine").type(JsonFieldType.BOOLEAN).description("자신의 리뷰인지 여부"),
						subsectionWithPath("data.pageable").type(JsonFieldType.STRING).description("페이지 정보"),
						//                                        fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 수"),
						//                                        fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
						//                                        subsectionWithPath("data.pageable.sort").type(JsonFieldType.STRING).description("정렬 정보"),
						//                                        fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("페이지 오프셋"),
						//                                        fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지 unpaged"),
						//                                        fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지 unpaged"),
						fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 번째 페이지 여부"),
						fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
						fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
						fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
						subsectionWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
						fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 요소 수"),
						fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("현재 페이지가 비어 있는지 여부")
					)
				)
			);
	}

}
