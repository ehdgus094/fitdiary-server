package im.fitdiary.server.exercise.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.server.exercise.data.entity.Exercise;
import im.fitdiary.server.exercise.presentation.dto.CreateExerciseReq;
import im.fitdiary.server.exercise.presentation.dto.ExerciseRes;
import im.fitdiary.server.exercise.presentation.dto.UpdateExerciseReq;
import im.fitdiary.server.exercise.service.ExerciseService;
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

@WebMvcTest(value = ExerciseController.class,
        // disable spring security
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
class ExerciseControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ExerciseService exerciseService;

    String BASE_URI = "/exercise";

    @BeforeEach
    void init() {
        TestUtils.setCustomAuthenticationToken();
    }

    @Test
    @DisplayName("create")
    void create() throws Exception {
        // given
        CreateExerciseReq req = ExerciseFactory.createExerciseReq();
        Exercise exercise = ExerciseFactory.exercise();
        ExerciseRes res = new ExerciseRes(exercise);
        given(exerciseService.create(any()))
                .willReturn(exercise);

        // when - then
        mvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.id")
                                .value(res.getId()),
                        jsonPath("$.data.name")
                                .value(res.getName()),
                        jsonPath("$.data.category")
                                .value(res.getCategory().toString()),
                        jsonPath("$.data.active")
                                .value(res.isActive())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("findById")
    void findById() throws Exception {
        // given
        long exerciseId = 1L;
        Exercise exercise = ExerciseFactory.exercise();
        ExerciseRes res = new ExerciseRes(exercise);
        given(exerciseService.findById(any(), anyLong()))
                .willReturn(exercise);

        // when - then
        mvc.perform(get(BASE_URI + "/" + exerciseId))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.id")
                                .value(res.getId()),
                        jsonPath("$.data.name")
                                .value(res.getName()),
                        jsonPath("$.data.category")
                                .value(res.getCategory().toString()),
                        jsonPath("$.data.active")
                                .value(res.isActive())
                )
                .andDo(print());
    }

    @Test
    @DisplayName("updateById")
    void updateById() throws Exception {
        // given
        long exerciseId = 1L;
        UpdateExerciseReq req = ExerciseFactory.updateExerciseReq();

        // when - then
        mvc.perform(put(BASE_URI + "/" + exerciseId)
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
        long exerciseId = 1L;

        // when - then
        mvc.perform(delete(BASE_URI + "/" + exerciseId))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print());
    }
}