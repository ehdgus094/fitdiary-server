package im.fitdiary.fitdiaryserver.auth.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.fitdiaryserver.auth.presentation.dto.LoginEmailUserReq;
import im.fitdiary.fitdiaryserver.auth.presentation.dto.LoginKakaoUserReq;
import im.fitdiary.fitdiaryserver.auth.service.AuthUserService;
import im.fitdiary.fitdiaryserver.auth.service.dto.JwtToken;
import im.fitdiary.fitdiaryserver.config.ConfigProperties;
import im.fitdiary.fitdiaryserver.security.jwt.filter.JwtAuthenticationFilter;
import im.fitdiary.fitdiaryserver.util.TestUtils;
import im.fitdiary.fitdiaryserver.util.factory.auth.AuthFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(value = ConfigProperties.class)
class AuthUserControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    AuthUserService authUserService;
    private final String BASE_URI = "/auth/user";

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
        given(authUserService.login(any()))
                .willReturn(jwtToken);

        // when - then
        mvc.perform(post(BASE_URI + "/login/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.accessToken")
                                .value(jwtToken.getAccessToken()),
                        jsonPath("$.data.refreshToken")
                                .value(jwtToken.getRefreshToken())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("loginKakaoUser")
    void loginKakaoUser() throws Exception {
        // given
        LoginKakaoUserReq req = AuthFactory.loginKakaoUserReq();
        JwtToken jwtToken = AuthFactory.jwtToken();
        given(authUserService.login(any()))
                .willReturn(jwtToken);

        // when - then
        mvc.perform(post(BASE_URI + "/login/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.accessToken")
                                .value(jwtToken.getAccessToken()),
                        jsonPath("$.data.refreshToken")
                                .value(jwtToken.getRefreshToken())
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
        given(authUserService.refreshToken(any(), anyString()))
                .willReturn(jwtToken);

        // when - then
        mvc.perform(post(BASE_URI + "/refresh-token")
                        .header("Authorization", "token"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.accessToken")
                                .value(jwtToken.getAccessToken()),
                        jsonPath("$.data.refreshToken")
                                .value(jwtToken.getRefreshToken())
                )
                .andDo(print());
    }
}