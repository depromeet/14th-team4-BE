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
import com.depromeet.domains.user.dto.request.NicknameRequest;
import com.depromeet.domains.user.dto.response.UserBookmarkResponse;
import com.depromeet.domains.user.dto.response.UserFeedResponse;
import com.depromeet.domains.user.dto.response.UserProfileResponse;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser
class UserControllerTest extends RestDocsTestSupport {

	@Test
	void updateUserNickname() throws Exception {
		// given
		NicknameRequest nicknameRequest = new NicknameRequest("뉴닉네임");

		doNothing().when(userService).updateUserNickname(any(), eq("뉴닉네임"));

		// when & then
		mockMvc.perform(
				put("/api/v1/users/nickname")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsBytes(nicknameRequest))
					.header("Authorization", "Bearer accessToken"))
			.andExpect(status().isOk())
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
		UserProfileResponse userProfileResponse = UserProfileResponse.of(1L,"닉네임","배고픈");
		given(userService.getUserProfile(any())).willReturn(userProfileResponse);

		// when
		mockMvc.perform(
				get("/api/v1/users/profile")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken"))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						subsectionWithPath("data").type(JsonFieldType.OBJECT).description("프로필"),
						fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
						fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
						fieldWithPath("data.level").type(JsonFieldType.STRING).description("유저 레벨 이름 (배고픈, 맨밥이, 또밥이, 또또밥이)"))
				)
			);
	}

	@Test
	void getMyBookmarks() throws Exception {
		// given
		UserBookmarkResponse userBookmarkResponse1 = UserBookmarkResponse.builder()
			.bookmarkId(1L)
			.storeId(1L)
			.storeName("칠기마라탕")
			.address("서울특별시 동대문구 제기로5길 38")
			.kakaoCategoryName("중식")
			.isVisited(true)
			.build();

		UserBookmarkResponse userBookmarkResponse2 = UserBookmarkResponse.builder()
			.bookmarkId(2L)
			.storeId(2L)
			.storeName("알베르")
			.address("서울특별시 강남구 강남대로102길 34")
			.kakaoCategoryName("카페")
			.isVisited(true)
			.build();

		UserBookmarkResponse userBookmarkResponse3 = UserBookmarkResponse.builder()
			.bookmarkId(3L)
			.storeId(3L)
			.storeName("떡도리탕")
			.address("서울특별시 강남구 테헤란로1길 28-9 1층")
			.kakaoCategoryName("한식")
			.isVisited(false)
			.build();

		List<UserBookmarkResponse> content = Arrays.asList(userBookmarkResponse1, userBookmarkResponse2,
			userBookmarkResponse3);
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
						fieldWithPath("data.content[].bookmarkId").type(JsonFieldType.NUMBER).description("북마크 ID"),
						fieldWithPath("data.content[].storeId").type(JsonFieldType.NUMBER).description("가게 ID"),
						fieldWithPath("data.content[].storeName").type(JsonFieldType.STRING).description("가게 이름"),
						fieldWithPath("data.content[].address").type(JsonFieldType.STRING).description("가게 주소"),
						fieldWithPath("data.content[].kakaoCategoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
						fieldWithPath("data.content[].isVisited").type(JsonFieldType.BOOLEAN)
							.description("유저의 가게 방문 여부"),
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
	void getMyFeeds() throws Exception {
		// given
		UserFeedResponse userFeedResponse1 = UserFeedResponse.builder()
			.feedId(1L)
			.storeId(1L)
			.storeName("칠기마라탕")
			.categoryName("중식")
			.createdAt(LocalDateTime.now())
			.rating(5)
			.imageUrl("https://image.com/1.jpg")
			.description("맛있어요")
			.build();

		UserFeedResponse userFeedResponse2 = UserFeedResponse.builder()
			.feedId(2L)
			.storeId(1L)
			.storeName("칠기마라탕")
			.createdAt(LocalDateTime.now())
			.categoryName("중식")
			.rating(4)
			.imageUrl("https://image.com/2.jpg")
			.description("맛있어요")
			.build();

		UserFeedResponse userFeedResponse3 = UserFeedResponse.builder()
			.feedId(3L)
			.storeId(2L)
			.storeName("알베르")
			.createdAt(LocalDateTime.now())
			.categoryName("카페")
			.rating(5)
			.imageUrl(null)
			.description("맛있어요")
			.build();

		List<UserFeedResponse> content = Arrays.asList(userFeedResponse1, userFeedResponse2, userFeedResponse3);
		Slice<UserFeedResponse> userReviewResponses = new SliceImpl<>(content, Pageable.unpaged(), true);

		given(userService.getUserFeeds(any(), any())).willReturn(userReviewResponses);

		// when
		mockMvc.perform(
				get("/api/v1/users/feeds")
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
						subsectionWithPath("data.content[]").type(JsonFieldType.ARRAY).description("피드 목록"),
						fieldWithPath("data.content[].feedId").type(JsonFieldType.NUMBER).description("피드 ID"),
						fieldWithPath("data.content[].storeId").type(JsonFieldType.NUMBER).description("가게 ID"),
						fieldWithPath("data.content[].storeName").type(JsonFieldType.STRING).description("가게 이름"),
						fieldWithPath("data.content[].rating").type(JsonFieldType.NUMBER).description("평점"),
						fieldWithPath("data.content[].categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
						fieldWithPath("data.content[].imageUrl").type(JsonFieldType.STRING)
							.description("이미지 URL")
							.optional(),
						fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING).description("피드 작성 일시"),
						fieldWithPath("data.content[].description").type(JsonFieldType.STRING).description("피드 내용"),
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
	public void deleteUserTest() throws Exception {
		doNothing().when(userService).deleteUser(any(), any());

		mockMvc.perform(delete("/api/v1/users/withdraw"))
			.andExpect(status().isOk());
	}
}
