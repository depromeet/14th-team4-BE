package com.depromeet.controller;

import com.depromeet.document.RestDocsTestSupport;
import com.depromeet.domains.feed.dto.response.FeedDetailResponse;
import com.depromeet.domains.feed.dto.response.FeedResponse;
import com.depromeet.domains.store.dto.StoreFeedResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser
public class FeedControllerTest extends RestDocsTestSupport {

    @Test
    void getFeeds() throws Exception {
        FeedResponse feedResponse1 = FeedResponse.builder()
                .userId(1L)
                .profileImg("profileImg")
                .nickname("kim")
                .storeId(1L)
                .storeName("버거킹")
                .kakaoCategoryName("패스트푸드")
                .address("서울시 강남구")
                .feedId(1L)
                .description("맛있어요")
                .feedImg("feedImg")
                .createdAt(LocalDateTime.now())
                .isFollowed(true)
                .build();

        FeedResponse feedResponse2 = FeedResponse.builder()
                .userId(2L)
                .profileImg("profileImg")
                .nickname("필환")
                .storeId(2L)
                .storeName("롯데리아")
                .kakaoCategoryName("패스트푸드")
                .address("서울시 강남구")
                .feedId(2L)
                .description("맛있어요11")
                .feedImg("feedImg")
                .createdAt(LocalDateTime.now())
                .isFollowed(false)
                .build();

        List<FeedResponse> content = Arrays.asList(feedResponse1, feedResponse2);
        Pageable pageable = PageRequest.of(0, 10);
        Slice<FeedResponse> feedResponses = new SliceImpl<>(content, pageable, true);

        given(feedService.getFeeds(anyLong(), any(), anyString(), anyInt())).willReturn(feedResponses);

        mockMvc.perform(
                        get("/api/v1/feeds")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer accessToken")
                                .param("type", "ALL")
                                .param("lastIdxId", "13")
                                .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                queryParameters(
                                        parameterWithName("type").description("피드 조회 타입"),
                                        parameterWithName("lastIdxId").description("마지막 원소 ID"),
                                        parameterWithName("size").description("페이지 크기")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                                        subsectionWithPath("data.content[]").type(JsonFieldType.ARRAY).description("리뷰 목록"),
                                        fieldWithPath("data.content[].userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                        fieldWithPath("data.content[].profileImg").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                                        fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("data.content[].storeId").type(JsonFieldType.NUMBER).description("가게 ID"),
                                        fieldWithPath("data.content[].storeName").type(JsonFieldType.STRING).description("가게 이름"),
                                        fieldWithPath("data.content[].kakaoCategoryName").type(JsonFieldType.STRING).description("카카오 카테고리 이름"),
                                        fieldWithPath("data.content[].address").type(JsonFieldType.STRING).description("주소"),
                                        fieldWithPath("data.content[].feedId").type(JsonFieldType.NUMBER).description("피드 ID"),
                                        fieldWithPath("data.content[].description").type(JsonFieldType.STRING).description("리뷰 내용"),
                                        fieldWithPath("data.content[].feedImg").type(JsonFieldType.STRING).description("이미지 URL"),
                                        fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                                        fieldWithPath("data.content[].isFollowed").type(JsonFieldType.BOOLEAN).description("자신의 리뷰인지 여부"),
                                        subsectionWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                        fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                        subsectionWithPath("data.pageable.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                                        fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비어 있는지 여부"),
                                        fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬이 되어 있는지 여부"),
                                        fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬이 되어 있지 않은지 여부"),
                                        fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("페이지 오프셋"),
                                        fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지가 페이징 되어 있는지 여부"),
                                        fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지가 페이징 되어 있지 않은지 여부"),
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
    void getFeed() throws Exception {
        FeedDetailResponse feedDetailResponse = FeedDetailResponse.builder()
                .userId(1L)
                .profileImg("image.url")
                .nickname("kim")
                .storeId(1L)
                .storeName("버거킹")
                .kakaoCategoryName("패스트푸드")
                .address("서울시 강남")
                .feedId(1L)
                .description("맛남요")
                .feedImg("image.url")
                .createdAt(LocalDateTime.now())
                .isFollowed(false)
                .rating(1).build();

        given(feedService.getFeed(any(), anyLong())).willReturn(feedDetailResponse);

        mockMvc.perform(
                        get("/api/v1/feeds/{feedId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("feedId").description("피드 ID")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                                        fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                        fieldWithPath("data.profileImg").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("data.storeId").type(JsonFieldType.NUMBER).description("가게 ID"),
                                        fieldWithPath("data.storeName").type(JsonFieldType.STRING).description("가게 이름"),
                                        fieldWithPath("data.kakaoCategoryName").type(JsonFieldType.STRING).description("카카오 카테고리 이름"),
                                        fieldWithPath("data.address").type(JsonFieldType.STRING).description("주소"),
                                        fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("피드 ID"),
                                        fieldWithPath("data.description").type(JsonFieldType.STRING).description("리뷰 내용"),
                                        fieldWithPath("data.feedImg").type(JsonFieldType.STRING).description("피드 이미지 URL"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("피드 생성일자"),
                                        fieldWithPath("data.isFollowed").type(JsonFieldType.BOOLEAN).description("팔로우 여부"),
                                        fieldWithPath("data.rating").type(JsonFieldType.NUMBER).description("평점")
                                )
                        )
                );
    }

    @Test
    void deleteStoreFeed() throws Exception {
        doNothing().when(feedService).deleteFeed(any(), anyLong());

        mockMvc.perform(delete("/api/v1/feeds/{feedId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("feedId").description("피드 ID")
                                )));
    }
}
