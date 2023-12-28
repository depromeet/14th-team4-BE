package com.depromeet.controller;

import com.depromeet.document.RestDocsTestSupport;
import com.depromeet.domains.test.dto.request.TestRequest;
import com.depromeet.domains.test.dto.response.TestResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
// 아래의 임포트를 사용해야함. RestDocumentationRequestBuilders.get() 이런식으로 사용해야함
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser // 해당 어노테이션이 있어야 인증 유저로 받아들임
public class TestControllerTest extends RestDocsTestSupport {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void create() throws Exception {
        // given
        TestRequest testRequest = TestRequest.builder()
                .title("title")
                .content("content")
                .build();

        TestResponse testResponse = TestResponse.builder()
                .id(1L)
                .title("title")
                .content("content")
                .build();

//        given(testService.create(any(TestRequest.class))).willReturn(testResponse); 되는 코드 꼭 any로 해줘야함. 그냥 값 넣으면 response data가 안찍힘
        given(testService.create(any())).willReturn(testResponse);
        // when & then
        mockMvc.perform(
                post("/test")
                        .with(csrf()) // Spring Security Test에서 csrf로 발생하는 403을  해결하기 위해
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer accessToken")
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                requestFields(
                                        fieldWithPath("title").description("제목"),
                                        fieldWithPath("content").description("내용")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("ID"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용")
                                )
                        )
                )
        ;
    }

    @Test
    public void findAll() throws Exception {
        // given
        TestResponse testResponse1 = TestResponse.builder()
                .id(1L)
                .title("title1")
                .content("content1")
                .build();

        TestResponse testResponse2 = TestResponse.builder()
                .id(2L)
                .title("title2")
                .content("content2")
                .build();

        List<TestResponse> responses = List.of(testResponse1, testResponse2);

        given(testService.findAll()).willReturn(responses);
        // when & then
        mockMvc.perform(
                        get("/test")
                                .with(csrf())
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
                                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("ID"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("data[].content").type(JsonFieldType.STRING).description("내용")
                                )
                        )
                )
        ;
    }

    @Test
    public void findById() throws Exception {
        // given
        TestResponse testResponse1 = TestResponse.builder()
                .id(1L)
                .title("title1")
                .content("content1")
                .build();

        given(testService.findById(eq(1L))).willReturn(testResponse1);
        // when & then
        mockMvc.perform(
                        get("/test/{testId}", 1L)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("testId").description("테스트 번호")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("ID"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용")
                                )
                        )
                )
        ;
    }

    @Test
    public void update() throws Exception {

        // given
        TestRequest testRequest = TestRequest.builder()
                .title("title")
                .content("content")
                .build();

        doNothing().when(testService).update(eq(1L), any(TestRequest.class));

        // when & then
        mockMvc.perform(
                        put("/test/{testId}", 1L)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(testRequest))
                                .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("testId").description("테스트 번호")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                requestFields(
                                        fieldWithPath("title").description("제목"),
                                        fieldWithPath("content").description("내용")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("data")
                                )
                        )
                )
        ;
    }

    @Test
    public void delete() throws Exception {
        // given

        doNothing().when(testService).delete(eq(1L));

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/test/{testId}", 1L) // delete mapping은 RestDocumentationRequestBuilders를 명시해줘야 함
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("testId").description("테스트 번호")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("data")
                                )
                        )
                )
        ;
    }
}
