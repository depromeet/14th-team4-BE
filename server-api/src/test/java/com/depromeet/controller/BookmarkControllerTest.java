package com.depromeet.controller;

import com.depromeet.document.RestDocsTestSupport;
import com.depromeet.domains.bookmark.dto.response.BookmarkingResponse;
import com.depromeet.domains.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser
class BookmarkControllerTest extends RestDocsTestSupport {

    @Test
    void createBookmark() throws Exception {
        //given
        Long storeId = 1L;
        BookmarkingResponse bookmarkingResponse = BookmarkingResponse.of(1L, 1L);

        given(bookmarkService.createBookmark(eq(storeId), any()))
                .willReturn(bookmarkingResponse);

        //when & then
        mockMvc.perform(
                        post("/api/v1/bookmarks/{storeId}", 1L)
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
                                        fieldWithPath("data.bookmarkId").type(JsonFieldType.NUMBER).description("북마크 ID"),
                                        fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("유저 ID")
                                )
                        )
                )
        ;
    }

    @Test
    void deleteBookmark() throws Exception {
        //given
        Long bookmarkId = 1L;
        doNothing().when(bookmarkService).deleteBookmark(eq(bookmarkId), any(User.class));

        //when & then
        mockMvc.perform(
                        delete("/api/v1/bookmarks/{bookmarkId}", bookmarkId) // HTTP 메서드를 DELETE로 변경
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("bookmarkId").description("북마크 ID") // 파라미터 이름을 bookmarkId로 변경
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("데이터 없음")
                                )
                        )
                )
        ;
    }
}