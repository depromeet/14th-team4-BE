package com.depromeet.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import com.depromeet.document.RestDocsTestSupport;
import com.depromeet.domains.follow.dto.FollowListResponse;
import com.depromeet.domains.follow.dto.FollowUpdateResponse;
import com.depromeet.enums.FollowType;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser
class FollowControllerTest extends RestDocsTestSupport {
	@Test
	void updateFollow() throws Exception {
		// given
		Long receiverId = 1L;
		given(followService.updateFollow(any(), eq(receiverId))).willReturn(
			FollowUpdateResponse.builder().followId(1L).senderId(1L).receiverId(2L).isFollow(true).build());
		// when
		mockMvc.perform(
				patch("/api/v1/follows/{receiverId}", receiverId)
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken"))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					pathParameters(
						parameterWithName("receiverId").description("팔로잉할, 팔로잉 취소할 user id")),
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						fieldWithPath("data.followId").type(JsonFieldType.NUMBER).description("팔로우 id"),
						fieldWithPath("data.senderId").type(JsonFieldType.NUMBER).description("팔로잉/언팔로잉 요청한 유저 id (나)"),
						fieldWithPath("data.receiverId").type(JsonFieldType.NUMBER).description("팔로잉/언팔로잉 당한 유저 id (상대방)"),
						fieldWithPath("data.isFollow").type(JsonFieldType.BOOLEAN).description("팔로잉 요청 = true, 언팔로잉 요청 = false")
				)
			)
		);
	}

	@Test
	void getFollowList() throws Exception {
		// given
		Long targetUserId = 4L;
		FollowListResponse followListResponse1 = FollowListResponse.builder()
			.userId(1L)
			.nickname("nickname")
			.profileImageUrl("profileImageUrl")
			.isFollowed(true)
			.build();
		FollowListResponse followListResponse2 = FollowListResponse.builder()
			.userId(2L)
			.nickname("nickname")
			.profileImageUrl("profileImageUrl")
			.isFollowed(false)
			.build();
		FollowListResponse followListResponse3 = FollowListResponse.builder()
			.userId(3L)
			.nickname("nickname")
			.profileImageUrl("profileImageUrl")
			.isFollowed(true)
			.build();

		List<FollowListResponse> followList = Arrays.asList(followListResponse1, followListResponse2,
			followListResponse3);

		given(followService.getFollowList(any(), eq(targetUserId), eq(FollowType.FOLLOWER))).willReturn(
			followList
		);

		// when
		mockMvc.perform(
				get("/api/v1/follows/{userId}", targetUserId)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken")
					.param("type", "FOLLOWER")
			)
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					pathParameters(
						parameterWithName("userId").description("팔로워/팔로잉 조회할 user id")),
					queryParameters(
						parameterWithName("type").description("FOLLOWER = 팔로워, FOLLOWING = 팔로잉")
					),
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						subsectionWithPath("data[]").type(JsonFieldType.ARRAY).description("팔로우 목록"),
						fieldWithPath("data[].userId").type(JsonFieldType.NUMBER).description("팔로잉/팔로워의 userid"),
						fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("nickname"),
						fieldWithPath("data[].profileImageUrl").type(JsonFieldType.STRING).description("profileImageUrl"),
						fieldWithPath("data[].isFollowed").type(JsonFieldType.BOOLEAN).description("내가 이 user를 팔로우하고 있는지")
					)
				)
			);
	}
}

