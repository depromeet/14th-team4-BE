package com.depromeet.document;

import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.depromeet.S3.S3UploaderService;
import com.depromeet.auth.controller.AuthController;
import com.depromeet.auth.jwt.JwtService;
import com.depromeet.auth.service.AuthService;
import com.depromeet.auth.service.CookieService;
import com.depromeet.domains.bookmark.controller.BookmarkController;
import com.depromeet.domains.bookmark.service.BookmarkService;
import com.depromeet.domains.image.controller.ImageController;
import com.depromeet.domains.store.controller.StoreController;
import com.depromeet.domains.store.controller.StoreSearchController;
import com.depromeet.domains.store.service.StoreSearchService;
import com.depromeet.domains.store.service.StoreService;
import com.depromeet.domains.user.controller.UserController;
import com.depromeet.domains.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Disabled
@WebMvcTest({
        // 테스트 하고자 하는 컨트롤러를 명시
        CommonDocController.class,
        StoreController.class,
        StoreSearchController.class,
        UserController.class,
        BookmarkController.class,
        AuthController.class,
        ImageController.class
})
public abstract class ControllerTest {

    @Autowired protected ObjectMapper objectMapper;

    @Autowired protected MockMvc mockMvc;

    @MockBean
    protected StoreService storeService;

    @MockBean
    protected StoreSearchService storeSearchService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected BookmarkService bookmarkService;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected CookieService cookieService;

    @MockBean
    protected JwtService jwtService;

    @MockBean
    protected S3UploaderService s3UploaderService;
    // @MockBean으로 필요한 레포지토리, 서비스로직을 정의

    protected String createJson(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }
}