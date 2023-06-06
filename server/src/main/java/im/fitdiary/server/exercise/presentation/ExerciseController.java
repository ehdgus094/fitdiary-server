package im.fitdiary.server.exercise.presentation;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.common.dto.Response;
import im.fitdiary.server.exception.e404.ExerciseNotFoundException;
import im.fitdiary.server.exercise.data.entity.Exercise;
import im.fitdiary.server.exercise.presentation.dto.CreateExerciseReq;
import im.fitdiary.server.exercise.presentation.dto.ExerciseRes;
import im.fitdiary.server.exercise.presentation.dto.UpdateExerciseReq;
import im.fitdiary.server.exercise.service.ExerciseService;
import im.fitdiary.server.security.argumentresolver.Auth;
import im.fitdiary.server.security.argumentresolver.AuthToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Exercise - Exercise")
@BaseMethodLogging
@RequiredArgsConstructor
@RequestMapping("/exercise/exercises")
@RestController
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Secured("ROLE_USER_ACCESS")
    @PostMapping
    public Response<ExerciseRes> create(@Auth AuthToken authToken, @RequestBody @Valid CreateExerciseReq req) {
        Exercise exercise = exerciseService.create(req.toDto(authToken.getId()));
        return new Response<>(new ExerciseRes(exercise));
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping("/{exerciseId}")
    public Response<ExerciseRes> findById(@Auth AuthToken authToken, @PathVariable("exerciseId") Long exerciseId)
            throws ExerciseNotFoundException {
        Exercise exercise = exerciseService.findById(exerciseId, authToken.getId());
        return new Response<>(new ExerciseRes(exercise));
    }

    @Secured("ROLE_USER_ACCESS")
    @PutMapping("/{exerciseId}")
    public Response<?> updateById(
            @Auth AuthToken authToken,
            @PathVariable("exerciseId") Long exerciseId,
            @RequestBody @Valid UpdateExerciseReq req
    ) throws ExerciseNotFoundException {
        exerciseService.updateById(exerciseId, authToken.getId(), req.toEditor());
        return new Response<>();
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/{exerciseId}")
    public Response<?> deleteById(@Auth AuthToken authToken, @PathVariable("exerciseId") Long exerciseId) {
        exerciseService.deleteById(exerciseId, authToken.getId());
        return new Response<>();
    }
}
