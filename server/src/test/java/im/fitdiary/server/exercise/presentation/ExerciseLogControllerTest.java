package im.fitdiary.server.exercise.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import im.fitdiary.server.exercise.presentation.dto.*;
import im.fitdiary.server.exercise.service.ExerciseLogService;
import im.fitdiary.server.security.jwt.filter.JwtAuthenticationFilter;
import im.fitdiary.server.util.TestUtils;
import im.fitdiary.server.util.factory.exercise.ExerciseFactory;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ExerciseLogController.class,
        // disable spring security
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
class ExerciseLogControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ExerciseLogService exerciseLogService;

    String BASE_URI = "/exercise/logs";

    @BeforeEach
    void init() {
        TestUtils.setCustomAuthenticationToken();
    }

    @Test
    @DisplayName("create")
    void create() throws Exception {
        // given
        CreateExerciseLogReq req = ExerciseFactory.createExerciseLogReq();
        ExerciseLog exerciseLog = ExerciseFactory.exerciseLog();
        ExerciseLogRes res = new ExerciseLogRes(exerciseLog);
        given(exerciseLogService.create(any()))
                .willReturn(exerciseLog);

        // when - then
        mvc.perform(post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.id")
                                .value(res.getId()),
                        jsonPath("$.data.duration")
                                .value(res.getDuration())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("findById")
    void findById() throws Exception {
        // given
        long exerciseLogId = 1L;
        ExerciseLog exerciseLog = ExerciseFactory.exerciseLog();
        ExerciseLogRes res = new ExerciseLogRes(exerciseLog);
        given(exerciseLogService.findById(any(), anyLong()))
                .willReturn(exerciseLog);

        // when - then
        mvc.perform(get(BASE_URI + "/" + exerciseLogId))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.id")
                                .value(res.getId()),
                        jsonPath("$.data.duration")
                                .value(res.getDuration()),
                        jsonPath("$.data.measuredAt")
                                .value(res.getMeasuredAt())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("deleteById")
    void deleteById() throws Exception {
        // given
        long exerciseLogId = 1L;

        // when - then
        mvc.perform(delete(BASE_URI + "/" + exerciseLogId))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }
}