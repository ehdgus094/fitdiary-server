package im.fitdiary.server.exercise.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.server.exercise.data.entity.Exercise;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import im.fitdiary.server.exercise.data.entity.ExerciseLogDetail;
import im.fitdiary.server.exercise.presentation.dto.CreateExerciseLogDetailListReq;
import im.fitdiary.server.exercise.presentation.dto.ExerciseLogDetailRes;
import im.fitdiary.server.exercise.presentation.dto.UpdateExerciseLogDetailListReq;
import im.fitdiary.server.exercise.service.ExerciseLogDetailService;
import im.fitdiary.server.exercise.service.dto.ExerciseLogDetailSlice;
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

@WebMvcTest(value = ExerciseLogDetailController.class,
        // disable spring security
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
class ExerciseLogDetailControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ExerciseLogDetailService exerciseLogDetailService;

    String BASE_URI = "/exercise/log-details";

    @BeforeEach
    void init() {
        TestUtils.setCustomAuthenticationToken();
    }

    @Test
    @DisplayName("createBulk")
    void createBulk() throws Exception {
        // given
        CreateExerciseLogDetailListReq req = ExerciseFactory.createExerciseLogDetailListReq();

        // when - then
        mvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }

    @Test
    @DisplayName("findById")
    void findById() throws Exception {
        // given
        long exerciseLogDetailId = 1L;
        Exercise exercise = ExerciseFactory.exercise();
        ExerciseLog exerciseLog = ExerciseFactory.exerciseLog();
        ExerciseLogDetail detail = ExerciseFactory.exerciseLogDetail(exercise, exerciseLog, 0);
        ExerciseLogDetailRes res = new ExerciseLogDetailRes(detail);
        given(exerciseLogDetailService.findById(any(), anyLong()))
                .willReturn(detail);

        // when - then
        mvc.perform(get(BASE_URI + "/" + exerciseLogDetailId))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.id")
                                .value(res.getId()),
                        jsonPath("$.data.sequence")
                                .value(res.getSequence()),
                        jsonPath("$.data.warmUp")
                                .value(res.isWarmUp()),
                        jsonPath("$.data.weight")
                                .value(res.getWeight()),
                        jsonPath("$.data.count")
                                .value(res.getCount()),
                        jsonPath("$.data.supportCount")
                                .value(res.getSupportCount())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("find")
    void find() throws Exception {
        // given
        ExerciseLogDetailSlice exerciseLogDetailSlice = ExerciseFactory.exerciseLogDetailSlice();
        given(exerciseLogDetailService.find(any(), any(), anyLong()))
                .willReturn(exerciseLogDetailSlice);

        // when - then
        mvc.perform(get(BASE_URI))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.content")
                                .isArray(),
                        jsonPath("$.data.content")
                                .isNotEmpty(),
                        jsonPath("$.data.hasNext")
                                .value(exerciseLogDetailSlice.isHasNext())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("updateBulk")
    void updateBulk() throws Exception {
        // given
        UpdateExerciseLogDetailListReq req = ExerciseFactory.updateExerciseLogDetailListReq();

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
        // given
        long exerciseLogDetailId = 1L;

        // when - then
        mvc.perform(delete(BASE_URI + "/" + exerciseLogDetailId))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }
}