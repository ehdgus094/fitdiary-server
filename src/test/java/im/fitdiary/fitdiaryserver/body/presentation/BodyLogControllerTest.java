package im.fitdiary.fitdiaryserver.body.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.fitdiaryserver.body.presentation.dto.CreateBodyLogReq;
import im.fitdiary.fitdiaryserver.body.service.BodyLogService;
import im.fitdiary.fitdiaryserver.body.service.dto.BodyLogSlice;
import im.fitdiary.fitdiaryserver.config.ConfigProperties;
import im.fitdiary.fitdiaryserver.security.jwt.filter.JwtAuthenticationFilter;
import im.fitdiary.fitdiaryserver.util.factory.body.BodyFactory;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = BodyLogController.class,
        // disable spring security
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@EnableConfigurationProperties(value = ConfigProperties.class)
class BodyLogControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BodyLogService bodyLogService;
    private final String BASE_URI = "/body/log";

    @Test
    @DisplayName("create")
    void create() throws Exception {
        // given
        CreateBodyLogReq req = BodyFactory.createBodyLogReq();

        // when - then
        mvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("searchLatest")
    void searchLatest() throws Exception {
        // given
        BodyLogSlice bodyLogSlice = BodyFactory.bodyLogSlice();
        given(bodyLogService.searchLatest(any(), any()))
                .willReturn(bodyLogSlice);

        // when - then
        mvc.perform(get(BASE_URI + "/search-latest"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.content")
                                .isArray(),
                        jsonPath("$.data.content")
                                .isNotEmpty(),
                        jsonPath("$.data.hasNext")
                                .value(bodyLogSlice.isHasNext())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("delete")
    void remove() throws Exception {
        // given
        long bodyLogId = 1L;

        // when - then
        mvc.perform(delete(BASE_URI + "/" + bodyLogId))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }
}