package com.depromeet.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import com.depromeet.document.RestDocsTestSupport;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser
class ImageControllerTest extends RestDocsTestSupport {

	@Test
	void getPresignedUrl() throws Exception {
		String presigendUrl = "https://ddoeatimg.kr.object.ncloudstorage.com/example";
		given(s3Service.getPreSignedUrl("exampleimage")).willReturn(presigendUrl);

		// when
		mockMvc.perform(
				get("/api/v1/images/presigned-url")
					.param("fileName", "exampleimage")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken")
			)
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					queryParameters(
						parameterWithName("fileName").description("업로드할 이미지 파일 이름")
					),
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						subsectionWithPath("data").type(JsonFieldType.OBJECT).description(""),
						fieldWithPath("data.presignedUrl").type(JsonFieldType.STRING).description("presigned url 주소"))
				)
			);
	}
}
