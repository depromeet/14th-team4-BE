package com.depromeet.controller;

import com.depromeet.document.RestDocsTestSupport;
import com.depromeet.domains.profile.dto.request.ProfileImageUrlRequest;
import com.depromeet.domains.profile.dto.request.ProfileNicknameRequest;
import com.depromeet.domains.profile.dto.response.ProfileFeedResponse;
import com.depromeet.domains.profile.dto.response.ProfileResponse;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser
class ProfileControllerTest extends RestDocsTestSupport {

    @Test
    void getProfile() throws Exception {
        //given
        ProfileResponse profileResponse = ProfileResponse.of(
                false
                , 4L
                , "https://image.com/1.jpg"
                , "닉네임"
                , 10
                , 10
                , 10
                , true);

        given(profileService.getProfile(any(), eq(3L))).willReturn(profileResponse);

        //when
        mockMvc.perform(
                get("/api/v1/profile/{userId}", 3L)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("userId").description("프로필 유저 ID")
                        ),
                        requestHeaders(
                            headerWithName("Authorization").description("accessToken")
                        ),
                        responseFields(
                            fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                            fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                            fieldWithPath("data.isMine").type(JsonFieldType.BOOLEAN).description("본인 프로필 여부"),
                            fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("프로필 유저 Id"),
                            fieldWithPath("data.profileImgUrl").type(JsonFieldType.STRING).description("프로필 imageUrl"),
                            fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("프로필 유저 닉네임"),
                            fieldWithPath("data.feedCnt").type(JsonFieldType.NUMBER).description("프로필 유저의 피드갯수"),
                            fieldWithPath("data.follwerCnt").type(JsonFieldType.NUMBER).description("프로필 유저의 팔로워수"),
                            fieldWithPath("data.followingCnt").type(JsonFieldType.NUMBER).description("프로필 유저의 팔로잉수"),
                            fieldWithPath("data.isFollowed").type(JsonFieldType.BOOLEAN).description("프로필 유저가 나를 팔로워했는지 여부")
                        )
                    )
                );
    }

    @Test
    void getProfileFeed() throws Exception {
        //given
        ProfileFeedResponse profileFeedResponse1 = ProfileFeedResponse.of(
                5L
                , 3L
                , 5L
                , "Store 5"
                , 567890123L
                , "image_url_5.jpg"
                , "서울특별시 강남구 동네1"
                , LocalDateTime.now()
                , 30L
                , 40L
                , false);

        ProfileFeedResponse profileFeedResponse2 = ProfileFeedResponse.of(
                6L
                , 3L
                , 6L
                , "Store 6"
                , 678901234L
                , "image_url_6.jpg"
                , "서울특별시 강남구 동네2"
                , LocalDateTime.now()
                , 35L
                , 45L
                , true);

        List<ProfileFeedResponse> content = Arrays.asList(profileFeedResponse1, profileFeedResponse2);
        Pageable pageable = PageRequest.of(0, 10);
        Slice<ProfileFeedResponse> profileFeedResponse = new SliceImpl<>(content, pageable, true);

        given(profileService.getProfileFeed(any(), anyLong(), anyLong(), anyInt())).willReturn(profileFeedResponse);

        //when
        mockMvc.perform(
                get("/api/v1/profile/{userId}/feeds", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer accessToken")
                        .param("lastIdxId", "13")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                queryParameters(
                                        parameterWithName("lastIdxId").description("마지막 원소 ID"),
                                        parameterWithName("size").description("페이지 크기")
                                ),
                                pathParameters(
                                        parameterWithName("userId").description("프로필 유저id")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                                        subsectionWithPath("data.content[]").type(JsonFieldType.ARRAY).description("리뷰 목록"),
                                        fieldWithPath("data.content[].feedId").type(JsonFieldType.NUMBER).description("피드 ID"),
                                        fieldWithPath("data.content[].userId").type(JsonFieldType.NUMBER).description("프로필 유저 ID"),
                                        fieldWithPath("data.content[].storeId").type(JsonFieldType.NUMBER).description("식당 고유 Id"),
                                        fieldWithPath("data.content[].storeName").type(JsonFieldType.STRING).description("식담명"),
                                        fieldWithPath("data.content[].kakaoStoreId").type(JsonFieldType.NUMBER).description("식당 카카오 고유 ID"),
                                        fieldWithPath("data.content[].feedImageUrl").type(JsonFieldType.STRING).description("피드 image URL"),
                                        fieldWithPath("data.content[].feedCreatedAt").type(JsonFieldType.STRING).description("피드 작성 시기"),
                                        fieldWithPath("data.content[].likeCnt").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                        fieldWithPath("data.content[].commentCnt").type(JsonFieldType.NUMBER).description("댓글 수"),
                                        fieldWithPath("data.content[].isHeartFeed").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
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
    void updateProfileNickname() throws Exception {
        //given
        ProfileNicknameRequest nicknameRequest = new ProfileNicknameRequest("뉴닉네임");

        doNothing().when(profileService).updateProfileNickname(any(), anyLong(), eq("뉴닉네임"));

        // when & then
        mockMvc.perform(
                        put("/api/v1/profile/{userId}/nickname", 3L)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(nicknameRequest))
                                .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("userId").description("프로필 유저 ID")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                requestFields(
                                        fieldWithPath("nickname").description("새로운 닉네임")
                                )
                        )
                );
    }

    @Test
    void updateProfileImageUrl() throws Exception {
        //given
        ProfileImageUrlRequest nicknameRequest = new ProfileImageUrlRequest("뉴 imageUrl");

        doNothing().when(profileService).updateProfileNickname(any(), anyLong(), eq("뉴 imageUrl"));

        // when & then
        mockMvc.perform(
                        put("/api/v1/profile/{userId}/img", 3L)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(nicknameRequest))
                                .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("userId").description("프로필 유저 ID")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                requestFields(
                                        fieldWithPath("profileImageUrl").description("프로필 이미지 url")
                                )
                        )
                )
        ;

    }
}
