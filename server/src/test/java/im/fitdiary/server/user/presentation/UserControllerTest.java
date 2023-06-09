package im.fitdiary.server.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.server.security.jwt.filter.JwtAuthenticationFilter;
import im.fitdiary.server.user.data.entity.User;
import im.fitdiary.server.user.presentation.dto.*;
import im.fitdiary.server.user.service.UserService;
import im.fitdiary.server.util.TestUtils;
import im.fitdiary.server.util.factory.user.UserFactory;
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

@WebMvcTest(value = UserController.class,
        // disable spring security
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    String BASE_URI = "/users";

    @BeforeEach
    void init() {
        TestUtils.setCustomAuthenticationToken();
    }

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
    @DisplayName("findById")
    void findById() throws Exception {
        // given
        User user = UserFactory.user();
        UserRes res = new UserRes(user);
        given(userService.findById(any()))
                .willReturn(user);

        // when - then
        mvc.perform(get(BASE_URI))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.name")
                                .value(res.getName()),
                        jsonPath("$.data.birthYmd")
                                .value(res.getBirthYmd()),
                        jsonPath("$.data.email")
                                .value(res.getEmail())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("updateById")
    void updateById() throws Exception {
        // given
        UpdateUserReq req = UserFactory.updateUserReq();

        // when - then
        mvc.perform(put(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }

    @Test
    @DisplayName("deleteById")
    void deleteById() throws Exception {
        // when - then
        mvc.perform(delete(BASE_URI))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }
}