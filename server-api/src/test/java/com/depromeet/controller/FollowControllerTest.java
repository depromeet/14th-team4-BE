package com.depromeet.controller;

import com.depromeet.document.RestDocsTestSupport;
import com.depromeet.domains.follow.dto.FollowUpdateResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
}

