package im.fitdiary.fitdiaryserver.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.fitdiaryserver.config.ConfigProperties;
import im.fitdiary.fitdiaryserver.security.CustomAuthenticationToken;
import im.fitdiary.fitdiaryserver.security.CustomUserDetails;
import im.fitdiary.fitdiaryserver.security.filter.JwtAuthenticationFilter;
import im.fitdiary.fitdiaryserver.user.dto.CreateEmailUserReq;
import im.fitdiary.fitdiaryserver.user.dto.LoginEmailUserReq;
import im.fitdiary.fitdiaryserver.user.dto.LoginUserRes;
import im.fitdiary.fitdiaryserver.user.service.UserService;
import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.security.core.context.SecurityContextHolder;
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

    @BeforeAll
    static void setAuthentication() {
        // @UserId 사용을 위한 인증정보 추가
        CustomUserDetails userDetails = new CustomUserDetails("1", null);
        CustomAuthenticationToken auth = new CustomAuthenticationToken(userDetails.getAuthorities(), userDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

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
                                .value(res.getAccessToken())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("조회")
    void find() throws Exception {
        // given

        // when - then
        mvc.perform(get(BASE_URI))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }
}