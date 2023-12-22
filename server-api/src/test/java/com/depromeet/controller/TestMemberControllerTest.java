package com.depromeet.controller;

import com.depromeet.document.DocumentLinkGenerator;
import com.depromeet.document.RestDocsTestSupport;
import com.depromeet.domains.test.dto.request.TestMemberSignUpRequest;
import com.depromeet.test.TestMemberStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.depromeet.config.RestDocsConfig.field;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import 문 주의 import를 다른것을 해서 오류가 나는 경우가 다수 존재
@AutoConfigureRestDocs // rest docs 자동 설정
class TestMemberControllerTest extends RestDocsTestSupport {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void member_get() throws Exception {

        mockMvc.perform(
                        get("/api/members/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo( // rest docs 문서 작성 시작
                        document("member-get", // 문서 조각 디렉토리 명
                                pathParameters( // path 파라미터 정보 입력
                                        parameterWithName("id").description("Member ID")
                                ),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").description("ID"),
                                        fieldWithPath("name").description("name"),
                                        fieldWithPath("email").description("email"),
                                        fieldWithPath("age").description("age"),
                                        // enum은 링크로 연결해서 별도로 처리
                                        fieldWithPath("status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.TEST_MEMBER_STATUS)),
                                        fieldWithPath("sex").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.TEST_SEX))
                                )
                        )
                )
        ;
    }

    @Test
    public void member_create() throws Exception {
        TestMemberSignUpRequest dto = TestMemberSignUpRequest.builder()
                .age(1)
                .email("example@naver.com")
                .status(TestMemberStatus.BAN)
                .build();

        mockMvc.perform(
                        post("/api/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson(dto)))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("age").description("age").attributes(field("constraints", "길이 10 이하")),
                                        fieldWithPath("email").description("email").attributes(field("constraints", "길이 30 이하")),
                                        fieldWithPath("status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.TEST_MEMBER_STATUS))
                                )
                        )
                )
        ;
    }

}
