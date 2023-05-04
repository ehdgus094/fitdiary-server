package im.fitdiary.fitdiaryserver.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.fitdiaryserver.config.ConfigProperties;
import im.fitdiary.fitdiaryserver.security.filter.JwtAuthenticationFilter;
import im.fitdiary.fitdiaryserver.user.dto.*;
import im.fitdiary.fitdiaryserver.user.service.UserService;
import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
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

@WebMvcTest(value = UserController.class,
        // disable spring security
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@EnableConfigurationProperties(value = ConfigProperties.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserService userService;
    private final String BASE_URI = "/user";

    @Test
    @DisplayName("이메일 유저 생성")
    void createEmail() throws Exception {
        // given
        CreateEmailUserReq req = UserFactory.createEmailUserReq();

        // when - then
        mvc.perform(post(BASE_URI + "/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 로그인")
    void loginEmail() throws Exception {
        // given
        LoginEmailUserReq req = UserFactory.loginEmailUserReq();
        LoginUserRes res = UserFactory.loginUserRes();
        given(userService.login(req.getLoginId(), req.getPassword()))
                .willReturn(res);

        // when - then
        mvc.perform(post(BASE_URI + "/login/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.accessToken")
                                .value(res.getAccessToken()),
                        jsonPath("$.data.refreshToken")
                                .value(res.getRefreshToken()),
                        jsonPath("$.data.user.name")
                                .value(res.getUser().getName()),
                        jsonPath("$.data.user.birthYmd")
                                .value(res.getUser().getBirthYmd())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("로그아웃")
    void logout() throws Exception {
        // given

        // when - then
        mvc.perform(post(BASE_URI + "/logout"))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }

    @Test
    @DisplayName("토큰 갱신")
    void refreshToken() throws Exception {
        // given
        RefreshTokenRes res = UserFactory.refreshTokenRes();
        given(userService.refreshToken(any(), anyString()))
                .willReturn(res);

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

    @Test
    @DisplayName("조회")
    void find() throws Exception {
        // given
        UserRes res = UserFactory.userRes();
        given(userService.findById(any()))
                .willReturn(res);

        // when - then
        mvc.perform(get(BASE_URI))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.name")
                                .value(res.getName()),
                        jsonPath("$.data.birthYmd")
                                .value(res.getBirthYmd())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("탈퇴")
    void remove() throws Exception {
        // given

        // when - then
        mvc.perform(delete(BASE_URI))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }
}