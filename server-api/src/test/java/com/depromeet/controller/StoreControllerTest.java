package com.depromeet.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
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
import com.depromeet.domains.store.dto.request.FeedRequest;
import com.depromeet.domains.store.dto.request.NewStoreRequest;
import com.depromeet.domains.store.dto.response.FeedAddResponse;

import com.depromeet.domains.store.dto.response.FeedAddLimitResponse;

import com.depromeet.domains.store.dto.response.StoreLocationRangeResponse;
import com.depromeet.domains.store.dto.response.StoreLocationRangeResponse.StoreLocationRange;
import com.depromeet.domains.store.dto.response.StorePreviewResponse;
import com.depromeet.domains.store.dto.response.StoreReportResponse;
import com.depromeet.domains.store.dto.response.StoreFeedResponse;
import com.depromeet.domains.store.dto.response.StoreSharingSpotResponse;
import com.depromeet.enums.CategoryType;
import com.depromeet.enums.FeedType;
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
			.kakaoCategoryName("중식")
			.storeName("칠기마라탕")
			.address("서울시 강남구 역삼동 123-123")
			.totalRating(4.1F)
			.totalFeedCnt(10L)
			.feedImageUrls(List.of("https://image.com/1.jpg", "https://image.com/2.jpg"))
			.userId(1L)
			.isBookmarked(true)
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
						fieldWithPath("data.kakaoCategoryName").type(JsonFieldType.STRING).description("카테고리 명"),
						fieldWithPath("data.storeName").type(JsonFieldType.STRING).description("음식점 명"),
						fieldWithPath("data.address").type(JsonFieldType.STRING).description("음식점 주소"),
						fieldWithPath("data.totalRating").type(JsonFieldType.NUMBER).description("음식점 별점"),
						fieldWithPath("data.totalFeedCnt").type(JsonFieldType.NUMBER).description("리뷰 개수"),
						fieldWithPath("data.feedImageUrls").type(JsonFieldType.ARRAY).description("리뷰 이미지 URL"),
						fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
						fieldWithPath("data.isBookmarked").type(JsonFieldType.BOOLEAN).description("북마크 여부")
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
			.thumbnailUrl("https://image.com/1.jpg")
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
						fieldWithPath("data.thumbnailUrl").type(JsonFieldType.STRING)
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
		StoreFeedResponse storeReviewResponse1 = StoreFeedResponse.builder()
			.userId(1L)
			.feedId(1L)
			.profileImageUrl("https://image.com/1.jpg")
			.nickName("홍길동")
			.rating(5)
			.feedImageUrl("https://image.com/1.jpg")
			.createdAt(LocalDate.now())
			.description("맛있어요")
			.isMine(true)
			.build();

		StoreFeedResponse storeReviewResponse2 = StoreFeedResponse.builder()
				.userId(2L)
				.feedId(2L)
				.profileImageUrl("https://image.com/1.jpg")
				.nickName("홍길동")
				.rating(5)
				.feedImageUrl("https://image.com/1.jpg")
				.createdAt(LocalDate.now())
				.description("맛있어요")
				.isMine(true)
				.build();

		StoreFeedResponse storeReviewResponse3 = StoreFeedResponse.builder()
				.userId(3L)
				.feedId(3L)
				.profileImageUrl("https://image.com/1.jpg")
				.nickName("홍길동")
				.rating(5)
				.feedImageUrl("https://image.com/1.jpg")
				.createdAt(LocalDate.now())
				.description("맛있어요")
				.isMine(true)
				.build();

		FeedType feedType = FeedType.REVISITED;

		List<StoreFeedResponse> content = Arrays.asList(storeReviewResponse1, storeReviewResponse2,
			storeReviewResponse3);
		Slice<StoreFeedResponse> storeReviewResponses = new SliceImpl<>(content, Pageable.unpaged(), true);

		given(storeService.getStoreReview(any(), eq(1L), eq(Optional.of(FeedType.REVISITED)),
			any(Pageable.class))).willReturn(storeReviewResponses);

		// when
		mockMvc.perform(
				get("/api/v1/stores/{storeId}/reviews", 1L)
					//                                .with(csrf())
					.param("type", FeedType.REVISITED.name())
					.param("page", "0")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken"))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					queryParameters(
						parameterWithName("page").description("페이지 번호 (0번 부터)"),
						parameterWithName("type").description("리뷰 타입 - REVISITED, PHOTO").optional()
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

	@Test
	public void createExistStoreReview() throws Exception {
		// given
		// storeId가 있는 경우
		FeedRequest requestWithStoreId = FeedRequest.builder()
			.storeId(1L)
			.rating(5)
			.imageUrl("https://exampleimageurl.com")
			.description("진짜진짜진짜진짜맛있어요")
			.build();

		FeedAddResponse reviewAddResponse = FeedAddResponse.of(7L, 1L);

		given(storeService.createStoreFeed(any(), any(FeedRequest.class))).willReturn(reviewAddResponse);
		// when & then
		mockMvc.perform(
				post("/api/v1/stores/feeds")
					.with(csrf()) // Spring Security Test에서 csrf로 발생하는 403을  해결하기 위해
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken")
					.content(objectMapper.writeValueAsString(requestWithStoreId)))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					requestFields(
						fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("가게 ID").optional(),
						fieldWithPath("newStore").type(JsonFieldType.OBJECT).description("새로운 가게 정보").optional(),
						fieldWithPath("newStore.storeName")
							.type(JsonFieldType.STRING)
							.description("가게 이름"),
						fieldWithPath("newStore.latitude").type(JsonFieldType.NUMBER).description("위도"),
						fieldWithPath("newStore.longitude").type(JsonFieldType.NUMBER).description("경도"),
						fieldWithPath("newStore.kakaoStoreId").type(JsonFieldType.NUMBER).description("카카오 가게 고유 ID"),
						fieldWithPath("newStore.kakaoCategoryName").type(JsonFieldType.STRING)
							.description("카카오 카테고리 분류 기준 (빵집, 뷔페 등 카카오에서 내려오는 값 저장하기 위함)"),
						fieldWithPath("newStore.address").type(JsonFieldType.STRING).description("가게 주소"),
						fieldWithPath("rating").type(JsonFieldType.NUMBER).description("별점"),
						fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("첨부된 이미지 url"),
						fieldWithPath("description").type(JsonFieldType.STRING).description("리뷰 내용")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						fieldWithPath("data.reviewId").type(JsonFieldType.NUMBER).description("생성된 리뷰 ID"),
						fieldWithPath("data.storeId").type(JsonFieldType.NUMBER).description("생성된/기존 가게 ID")
					)
				)
			)
		;
	}

	@Test
	public void createNewStoreReview() throws Exception {
		// given
		FeedRequest requestWithNewStore = FeedRequest.builder()
			.newStore(
				NewStoreRequest.builder()
					.storeName("칠기마라탕")
					.latitude(127.239487)
					.longitude(37.29472)
					.kakaoStoreId(1234L)
					.kakaoCategoryName("간식")
					.address("서울시 강남구 역삼동 123-123")
					.build())
			.rating(5)
			.imageUrl("https://exampleimageurl.com")
			.description("진짜진짜진짜진짜맛있어요")
			.build();

		FeedAddResponse reviewAddResponse = FeedAddResponse.of(7L, 3L);

		given(storeService.createStoreFeed(any(), any(FeedRequest.class))).willReturn(reviewAddResponse);

		// when & then
		mockMvc.perform(
				post("/api/v1/stores/feeds")
					.with(csrf()) // Spring Security Test에서 csrf로 발생하는 403을  해결하기 위해
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken")
					.content(objectMapper.writeValueAsString(requestWithNewStore)))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					requestFields(
						fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("가게 ID").optional(),
						fieldWithPath("newStore").type(JsonFieldType.OBJECT).description("새로운 가게 정보"),
						fieldWithPath("newStore.storeName")
							.type(JsonFieldType.STRING)
							.description("가게 이름"),
						fieldWithPath("newStore.latitude").type(JsonFieldType.NUMBER).description("위도"),
						fieldWithPath("newStore.longitude").type(JsonFieldType.NUMBER).description("경도"),
						fieldWithPath("newStore.kakaoStoreId").type(JsonFieldType.NUMBER).description("카카오 가게 고유 ID"),
						fieldWithPath("newStore.kakaoCategoryName").type(JsonFieldType.STRING)
							.description("카카오 카테고리 분류 기준 (빵집, 뷔페 등 카카오에서 내려오는 값 저장하기 위함)"),
						fieldWithPath("newStore.address").type(JsonFieldType.STRING).description("가게 주소"),
						fieldWithPath("rating").type(JsonFieldType.NUMBER).description("별점"),
						fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("첨부된 이미지 url"),
						fieldWithPath("description").type(JsonFieldType.STRING).description("리뷰 내용")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						fieldWithPath("data.reviewId").type(JsonFieldType.NUMBER).description("생성된 리뷰 ID"),
						fieldWithPath("data.storeId").type(JsonFieldType.NUMBER).description("생성된 가게 ID").optional()
					)
				)
			)
		;
	}

	@Test
	@DisplayName("맵의 위경도 내 식당들 정보 조회")
	public void getLocationRangeStores() throws Exception {

		Double leftTopLatitude = 60.11111;
		Double leftTopLongitude = 120.0000;
		Double rightBottomLatitude = 30.11111;
		Double rightBottomLongitude = 140.0000;
		int level = 4;
		Long userId = 1L; // userId 로 하면 null

		//given
		StoreLocationRange storeLocationRange1 = StoreLocationRange.builder()
			.storeId(1L)
			.kakaoStoreId(2L)
			.storeName("칠기마라탕1")
			.address("서울특별시 1")
			.longitude(127.239487)
			.latitude(37.29472)
			.totalFeedCount(1L)
			.isBookmarked(true)
			.build();

		StoreLocationRange storeLocationRange2 = StoreLocationRange.builder()
			.storeId(2L)
			.kakaoStoreId(2L)
			.storeName("칠기마라탕2")
			.address("서울특별시 2")
			.longitude(127.239487)
			.latitude(37.29472)
			.totalFeedCount(1L)
			.isBookmarked(true)
			.build();

		StoreLocationRange storeLocationRange3 = StoreLocationRange.builder()
			.storeId(3L)
			.kakaoStoreId(3L)
			.storeName("칠기마라탕3")
			.address("서울특별시 3")
			.longitude(127.239487)
			.latitude(37.29472)
			.totalFeedCount(1L)
			.isBookmarked(false)
			.build();

		StoreLocationRange storeLocationRange4 = StoreLocationRange.builder()
			.storeId(4L)
			.kakaoStoreId(4L)
			.storeName("칠기마라탕4")
			.address("서울특별시 4")
			.longitude(127.239487)
			.latitude(37.29472)
			.totalFeedCount(1L)
			.isBookmarked(false)
			.build();

		List<StoreLocationRange> bookMarkList = Arrays.asList(storeLocationRange1, storeLocationRange2);
		List<StoreLocationRange> locationRangeList = Arrays.asList(storeLocationRange3, storeLocationRange4);

		List<StoreLocationRange> totalList = new ArrayList<>();
		totalList.addAll(bookMarkList);
		totalList.addAll(locationRangeList);

		StoreLocationRangeResponse storeLocationRangeResponse =
			StoreLocationRangeResponse.of(totalList);

		// given
		given(storeService.getRangeStores(eq(leftTopLatitude), eq(leftTopLongitude), eq(rightBottomLatitude)
			, eq(rightBottomLongitude), eq(level), any()))
			.willReturn(storeLocationRangeResponse);

		// when
		mockMvc.perform(
				get("/api/v1/stores/location-range")
					.contentType(MediaType.APPLICATION_JSON)
					// .with(csrf())
					.header("Authorization", "Bearer accessToken")
					.param("leftTopLatitude", String.valueOf(leftTopLatitude))
					.param("leftTopLongitude", String.valueOf(leftTopLongitude))
					.param("rightBottomLatitude", String.valueOf(rightBottomLatitude))
					.param("rightBottomLongitude", String.valueOf(rightBottomLongitude))
					.param("level", String.valueOf(level)))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					queryParameters(
						parameterWithName("leftTopLatitude").description("첫번째 위도(좌측최상단)"),
						parameterWithName("leftTopLongitude").description("두번째 경도(좌측최상단)"),
						parameterWithName("rightBottomLatitude").description("두번째 위도(우측최하단)"),
						parameterWithName("rightBottomLongitude").description("두번째 경도(우측최하단)"),
						parameterWithName("level").description("확대/축소 레벨")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						// bookMarkList
						// locationStoreList
						fieldWithPath("data.locationStoreList[]").type(JsonFieldType.ARRAY)
							.description("요청한 위경도 내 식당 목록"),
						fieldWithPath("data.locationStoreList[].storeId").type(JsonFieldType.NUMBER)
							.description("우리 DB상 음식점 ID"),
						fieldWithPath("data.locationStoreList[].kakaoStoreId").type(JsonFieldType.NUMBER)
							.description("카카오 DB상 음식점 ID"),
						fieldWithPath("data.locationStoreList[].storeName").type(JsonFieldType.STRING)
							.description("음식점 명"),
						fieldWithPath("data.locationStoreList[].address").type(JsonFieldType.STRING)
							.description("음식점 주소"),
						fieldWithPath("data.locationStoreList[].longitude").type(JsonFieldType.NUMBER)
							.description("음식점 위도"),
						fieldWithPath("data.locationStoreList[].latitude").type(JsonFieldType.NUMBER)
							.description("음식점 경도"),
						fieldWithPath("data.locationStoreList[].totalFeedCount").type(JsonFieldType.NUMBER)
							.description("총 피드 갯수"),
						fieldWithPath("data.locationStoreList[].isBookmarked").type(JsonFieldType.BOOLEAN)
							.description("북마크 여부")
					)
				)
			);
	}

	@Test
	@DisplayName("나의 재방문한 식당 공유하기")
	public void getSharingSpots() throws Exception {

		String userNickName = "또밥입니다";
		Long userId = 3L;

		//given
		StoreSharingSpotResponse.StoreSharingSpot storeSharingSpot1 =
			StoreSharingSpotResponse.StoreSharingSpot.builder()
				.storeId(1L)
				.kakaoStoreId(2L)
				.storeName("칠기마라탕1")
				.address("서울특별시 1")
				.longitude(127.239487)
				.latitude(37.29472)
				.totalFeedCnt(1L)
				.build();

		StoreSharingSpotResponse.StoreSharingSpot storeSharingSpot2 =
			StoreSharingSpotResponse.StoreSharingSpot.builder()
				.storeId(2L)
				.kakaoStoreId(2L)
				.storeName("칠기마라탕2")
				.address("서울특별시 2")
				.longitude(127.239487)
				.latitude(37.29472)
				.totalFeedCnt(1L)
				.build();

		StoreSharingSpotResponse.StoreSharingSpot storeSharingSpot3 =
			StoreSharingSpotResponse.StoreSharingSpot.builder()
				.storeId(3L)
				.kakaoStoreId(3L)
				.storeName("칠기마라탕3")
				.address("서울특별시 3")
				.longitude(127.239487)
				.latitude(37.29472)
				.totalFeedCnt(1L)
				.build();

		StoreSharingSpotResponse.StoreSharingSpot storeSharingSpot4 =
			StoreSharingSpotResponse.StoreSharingSpot.builder()
				.storeId(4L)
				.kakaoStoreId(4L)
				.storeName("칠기마라탕4")
				.address("서울특별시 4")
				.longitude(127.239487)
				.latitude(37.29472)
				.totalFeedCnt(1L)
				.build();

		List<StoreSharingSpotResponse.StoreSharingSpot> storeSharingSpotList = Arrays.asList(
			storeSharingSpot1, storeSharingSpot2, storeSharingSpot3, storeSharingSpot4);

		StoreSharingSpotResponse locationStoreList =
			StoreSharingSpotResponse.of(userNickName, storeSharingSpotList);

		// given
		given(storeService.getSharingSpots(eq(userId)))
			.willReturn(locationStoreList);

		// when
		mockMvc.perform(
				get("/api/v1/stores/sharing-spot")
					.param("userId", String.valueOf(userId))
					.contentType(MediaType.APPLICATION_JSON))
			// .with(csrf())

			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					queryParameters(
						parameterWithName("userId").description("유저 고유id")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						fieldWithPath("data.userNickName").type(JsonFieldType.STRING)
							.description("유저닉네임 "),
						fieldWithPath("data.locationStoreList[]").type(JsonFieldType.ARRAY)
							.description("요청한 위경도 내 식당 목록"),
						fieldWithPath("data.locationStoreList[].storeId").type(JsonFieldType.NUMBER)
							.description("우리 DB상 음식점 ID"),
						fieldWithPath("data.locationStoreList[].kakaoStoreId").type(JsonFieldType.NUMBER)
							.description("카카오 DB상 음식점 ID"),
						fieldWithPath("data.locationStoreList[].storeName").type(JsonFieldType.STRING)
							.description("음식점 명"),
						fieldWithPath("data.locationStoreList[].address").type(JsonFieldType.STRING)
							.description("음식점 주소"),
						fieldWithPath("data.locationStoreList[].longitude").type(JsonFieldType.NUMBER)
							.description("음식점 위도"),
						fieldWithPath("data.locationStoreList[].latitude").type(JsonFieldType.NUMBER)
							.description("음식점 경도"),
						fieldWithPath("data.locationStoreList[].totalFeedCnt").type(JsonFieldType.NUMBER)
							.description("총 피드 갯수")
					)
				)
			);
	}

	@Test
	void getUserDailyStoreReviewLimit() throws Exception {
		// given
		given(storeService.checkUserDailyStoreFeedLimit(any(), eq(1L))).willReturn(FeedAddLimitResponse.of(false));

		// when
		mockMvc.perform(
				get("/api/v1/stores/{storeId}/feeds/check-limit", 1L)
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
						fieldWithPath("data").type(JsonFieldType.OBJECT).description("리뷰 작성 가능 여부 반환"),
						fieldWithPath("data.isAvailable").type(JsonFieldType.BOOLEAN)
							.description("리뷰 작성 가능 여부 반환 (작성가능 : true, 작성불가 : false)")
					)
				)
			);
	}

	@Test
	void deleteReview() throws Exception {
		// given
		Long reviewId = 1L;

		doNothing().when(storeService).deleteStoreReview(any(), eq(reviewId));

		// when
		mockMvc.perform(
				delete("/api/v1/reviews/{reviewId}", reviewId)
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken"))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					pathParameters(
						parameterWithName("reviewId").description("리뷰 ID")
					),
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						fieldWithPath("data").type(JsonFieldType.NULL).description("null")
					)
				)
			);
	}
}
