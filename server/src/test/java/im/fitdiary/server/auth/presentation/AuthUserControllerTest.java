package im.fitdiary.server.auth.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.server.auth.presentation.dto.LoginEmailUserReq;
import im.fitdiary.server.auth.presentation.dto.LoginKakaoUserReq;
import im.fitdiary.server.auth.presentation.dto.LoginUserRes;
import im.fitdiary.server.auth.presentation.dto.RefreshTokenUserRes;
import im.fitdiary.server.auth.service.AuthUserService;
import im.fitdiary.server.auth.service.dto.JwtToken;
import im.fitdiary.server.security.jwt.filter.JwtAuthenticationFilter;
import im.fitdiary.server.util.TestUtils;
import im.fitdiary.server.util.factory.auth.AuthFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebMvcTest(value = AuthUserController.class,
        // disable spring security
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
class AuthUserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    AuthUserService authUserService;

    String BASE_URI = "/auth/user";

    @BeforeEach
    void init() {
        TestUtils.setCustomAuthenticationToken();
    }

    @Test
    @DisplayName("loginEmailUser")
    void loginEmailUser() throws Exception {
        // given
        LoginEmailUserReq req = AuthFactory.loginEmailUserReq();
        JwtToken jwtToken = AuthFactory.jwtToken();
        LoginUserRes res = new LoginUserRes(jwtToken);
        given(authUserService.login(any()))
                .willReturn(jwtToken);

        // when - then
        mvc.perform(post(BASE_URI + "/login/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.accessToken")
                                .value(res.getAccessToken()),
                        jsonPath("$.data.refreshToken")
                                .value(res.getRefreshToken())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("loginKakaoUser")
    void loginKakaoUser() throws Exception {
        // given
        LoginKakaoUserReq req = AuthFactory.loginKakaoUserReq();
        JwtToken jwtToken = AuthFactory.jwtToken();
        LoginUserRes res = new LoginUserRes(jwtToken);
        given(authUserService.login(any()))
                .willReturn(jwtToken);

        // when - then
        mvc.perform(post(BASE_URI + "/login/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.accessToken")
                                .value(res.getAccessToken()),
                        jsonPath("$.data.refreshToken")
                                .value(res.getRefreshToken())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("logoutUser")
    void logoutUser() throws Exception {
        // when - then
        mvc.perform(post(BASE_URI + "/logout"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("refreshTokenUser")
    void refreshTokenUser() throws Exception {
        // given
        JwtToken jwtToken = AuthFactory.jwtToken();
        RefreshTokenUserRes res = new RefreshTokenUserRes(jwtToken);
        given(authUserService.refreshToken(any(), anyString()))
                .willReturn(jwtToken);

        // when - then
        mvc.perform(post(BASE_URI + "/refresh-token")
                        .header("Authorization", "token"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.accessToken")
                                .value(res.getAccessToken()),
                        jsonPath("$.data.refreshToken")
                                .value(res.getRefreshToken())
                )
                .andDo(print());
    }
}