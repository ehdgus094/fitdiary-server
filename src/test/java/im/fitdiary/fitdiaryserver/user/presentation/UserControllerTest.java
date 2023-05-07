package im.fitdiary.fitdiaryserver.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.fitdiaryserver.config.ConfigProperties;
import im.fitdiary.fitdiaryserver.security.jwt.filter.JwtAuthenticationFilter;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.presentation.dto.*;
import im.fitdiary.fitdiaryserver.user.service.UserService;
import im.fitdiary.fitdiaryserver.user.service.dto.AuthToken;
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
    @DisplayName("createEmail")
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
    @DisplayName("createKakao")
    void createKakao() throws Exception {
        // given
        CreateKakaoUserReq req = UserFactory.createKakaoUserReq();

        // when - then
        mvc.perform(post(BASE_URI + "/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("loginEmail")
    void loginEmail() throws Exception {
        // given
        LoginEmailUserReq req = UserFactory.loginEmailUserReq();
        AuthToken authToken = UserFactory.authToken();
        given(userService.login(any()))
                .willReturn(authToken);

        // when - then
        mvc.perform(post(BASE_URI + "/login/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.accessToken")
                                .value(authToken.getAccessToken()),
                        jsonPath("$.data.refreshToken")
                                .value(authToken.getRefreshToken())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("loginKakao")
    void loginKakao() throws Exception {
        // given
        LoginKakaoUserReq req = UserFactory.loginKakaoUserReq();
        AuthToken authToken = UserFactory.authToken();
        given(userService.login(any()))
                .willReturn(authToken);

        // when - then
        mvc.perform(post(BASE_URI + "/login/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.accessToken")
                                .value(authToken.getAccessToken()),
                        jsonPath("$.data.refreshToken")
                                .value(authToken.getRefreshToken())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("logout")
    void logout() throws Exception {
        // when - then
        mvc.perform(post(BASE_URI + "/logout"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("refreshToken")
    void refreshToken() throws Exception {
        // given
        AuthToken authToken = UserFactory.authToken();
        given(userService.refreshToken(any(), anyString()))
                .willReturn(authToken);

        // when - then
        mvc.perform(post(BASE_URI + "/refresh-token")
                        .header("Authorization", "token"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.accessToken")
                                .value(authToken.getAccessToken()),
                        jsonPath("$.data.refreshToken")
                                .value(authToken.getRefreshToken())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("find")
    void find() throws Exception {
        // given
        User user = UserFactory.emailUser();
        given(userService.findById(any()))
                .willReturn(user);

        // when - then
        mvc.perform(get(BASE_URI))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.name")
                                .value(user.getName()),
                        jsonPath("$.data.birthYmd")
                                .value(user.getBirthYmd()),
                        jsonPath("$.data.email")
                                .value(user.getEmail())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("remove")
    void remove() throws Exception {
        // when - then
        mvc.perform(delete(BASE_URI))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }
}