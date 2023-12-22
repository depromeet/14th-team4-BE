package com.depromeet.document;

import com.depromeet.controller.TestMemberController;
import com.depromeet.domains.home.controller.HomeController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@WebMvcTest({
        // 테스트 하고자 하는 컨트롤러를 명시
        HomeController.class,
        CommonDocController.class,
        TestMemberController.class,
})
public abstract class ControllerTest {

    @Autowired protected ObjectMapper objectMapper;

    @Autowired protected MockMvc mockMvc;

    // @MockBean으로 필요한 레포지토리, 서비스로직을 정의

    protected String createJson(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }
}