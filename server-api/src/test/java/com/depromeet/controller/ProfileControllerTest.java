package com.depromeet.controller;

import com.depromeet.document.RestDocsTestSupport;
import com.depromeet.domains.profile.dto.response.ProfileResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
                            parameterWithName("userId").description("프로필 유저 id")
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
}
