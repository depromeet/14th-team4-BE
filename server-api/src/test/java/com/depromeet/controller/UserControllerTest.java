package com.depromeet.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
import com.depromeet.domains.store.dto.response.StoreReviewResponse;
import com.depromeet.domains.test.dto.request.TestRequest;
import com.depromeet.domains.user.dto.request.NicknameRequest;
import com.depromeet.domains.user.dto.response.UserBookmarkResponse;
import com.depromeet.domains.user.dto.response.UserReviewResponse;
import com.depromeet.enums.ReviewType;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser
class UserControllerTest extends RestDocsTestSupport {

	@Test
	void updateUserNickname() throws Exception {
		// given
		NicknameRequest nicknameRequest = new NicknameRequest("뉴닉네임");

		doNothing().when(userService).updateUserNickname(eq(1L), eq("뉴닉네임"));

		// when & then
		mockMvc.perform(
				put("/api/v1/users/nickname")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsBytes(nicknameRequest))
					.header("Authorization", "Bearer accessToken"))
			.andExpect(status().isNoContent())
			.andDo(
				restDocs.document(
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					requestFields(
						fieldWithPath("nickname").description("새로운 닉네임")
					)
				)
			)
		;
	}

	@Test
	void getUserProfile() throws Exception {
	}

	@Test
	void getMyBookmarks() throws Exception {
		// given
		UserBookmarkResponse userBookmarkResponse1 = UserBookmarkResponse.builder()
			.storeId(1L)
			.storeName("칠기마라탕")
			.address("서울특별시 동대문구 제기로5길 38")
			.totalRevisitedCount(3L)
			.categoryName("중식")
			.isVisited(true)
			.build();

		UserBookmarkResponse userBookmarkResponse2 = UserBookmarkResponse.builder()
			.storeId(2L)
			.storeName("알베르")
			.address("서울특별시 강남구 강남대로102길 34")
			.totalRevisitedCount(0L)
			.categoryName("카페")
			.isVisited(true)
			.build();

		UserBookmarkResponse userBookmarkResponse3 = UserBookmarkResponse.builder()
			.storeId(3L)
			.storeName("떡도리탕")
			.address("서울특별시 강남구 테헤란로1길 28-9 1층")
			.totalRevisitedCount(50L)
			.categoryName("한식")
			.isVisited(false)
			.build();

		List<UserBookmarkResponse> content = Arrays.asList(userBookmarkResponse1, userBookmarkResponse2, userBookmarkResponse3);
		Slice<UserBookmarkResponse> userBookmarkResponses = new SliceImpl<>(content, Pageable.unpaged(), true);

		given(userService.getUserBookmarks(any(), any())).willReturn(userBookmarkResponses);

		// when
		mockMvc.perform(
				get("/api/v1/users/bookmarks")
					.param("page", "0")
					.param("size", "20")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken"))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					queryParameters(
						parameterWithName("page").description("페이지 번호 (1번 부터)"),
						parameterWithName("size").description("페이지 사이즈")
					),
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						subsectionWithPath("data.content[]").type(JsonFieldType.ARRAY).description("북마크 목록"),
						fieldWithPath("data.content[].storeId").type(JsonFieldType.NUMBER).description("가게 ID"),
						fieldWithPath("data.content[].storeName").type(JsonFieldType.STRING).description("가게 이름"),
						fieldWithPath("data.content[].address").type(JsonFieldType.STRING).description("가게 주소"),
						fieldWithPath("data.content[].categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
						fieldWithPath("data.content[].totalRevisitedCount").type(JsonFieldType.NUMBER).description("총 재방문한 사람 수"),
						fieldWithPath("data.content[].isVisited").type(JsonFieldType.BOOLEAN).description("유저의 가게 방문 여부"),
						subsectionWithPath("data.pageable").type(JsonFieldType.STRING).description("페이지 요청 정보"),
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

	@Test
	void getMyReviews() throws Exception {
		// given
		UserReviewResponse userReviewResponse1 = UserReviewResponse.builder()
			.storeId(1L)
			.storeName("칠기마라탕")
			.visitTimes(1)
			.visitedAt(LocalDate.of(2024,1,11))
			.categoryName("중식")
			.rating(5)
			.imageUrl("https://image.com/1.jpg")
			.description("맛있어요")
			.build();

		UserReviewResponse userReviewResponse2 = UserReviewResponse.builder()
			.storeId(1L)
			.storeName("칠기마라탕")
			.visitTimes(2)
			.visitedAt(LocalDate.of(2024,1,13))
			.categoryName("중식")
			.rating(4)
			.imageUrl("https://image.com/2.jpg")
			.description("맛있어요")
			.build();

		UserReviewResponse userReviewResponse3 = UserReviewResponse.builder()
			.storeId(2L)
			.storeName("알베르")
			.visitTimes(1)
			.visitedAt(LocalDate.of(2024,1,3))
			.categoryName("카페")
			.rating(5)
			.imageUrl(null)
			.description("맛있어요")
			.build();

		List<UserReviewResponse> content = Arrays.asList(userReviewResponse1, userReviewResponse2, userReviewResponse3);
		Slice<UserReviewResponse> userReviewResponses = new SliceImpl<>(content, Pageable.unpaged(), true);

		given(userService.getUserReviews(any(), any())).willReturn(userReviewResponses);

		// when
		mockMvc.perform(
				get("/api/v1/users/reviews")
					.param("page", "0")
					.param("size", "20")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken"))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					queryParameters(
						parameterWithName("page").description("페이지 번호 (1번 부터)"),
						parameterWithName("size").description("페이지 사이즈")
					),
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						subsectionWithPath("data.content[]").type(JsonFieldType.ARRAY).description("리뷰 목록"),
						fieldWithPath("data.content[].storeId").type(JsonFieldType.NUMBER).description("가게 ID"),
						fieldWithPath("data.content[].storeName").type(JsonFieldType.STRING).description("가게 이름"),
						fieldWithPath("data.content[].rating").type(JsonFieldType.NUMBER).description("평점"),
						fieldWithPath("data.content[].categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
						fieldWithPath("data.content[].imageUrl").type(JsonFieldType.STRING).description("이미지 URL").optional(),
						fieldWithPath("data.content[].visitTimes").type(JsonFieldType.NUMBER).description("방문 횟수"),
						fieldWithPath("data.content[].visitedAt").type(JsonFieldType.STRING).description("방문 일시"),
						fieldWithPath("data.content[].description").type(JsonFieldType.STRING).description("리뷰 내용"),
						subsectionWithPath("data.pageable").type(JsonFieldType.STRING).description("페이지 요청 정보"),
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
