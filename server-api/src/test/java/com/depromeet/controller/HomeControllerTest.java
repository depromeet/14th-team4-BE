package com.depromeet.controller;

import com.depromeet.document.RestDocsTestSupport;
import com.depromeet.domains.home.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.depromeet.config.RestDocsConfig.field;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser
//@TestPropertySource(locations = "classpath:application-test.yml")
public class HomeControllerTest extends RestDocsTestSupport {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void Hello_테스트() throws Exception {

        // given (pathvariable)
        String name = "디프만 4팀 서버 파이팅!!";

        // when
        mockMvc.perform(
                get("/hello/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // then
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("name").description("이름").attributes(field("constraints", "이름만 적으시오"))
                                )
                        )
                )
        ;
    }
}