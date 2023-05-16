package im.fitdiary.fitdiaryserver.exercise.presentation;

import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.exercise.data.entity.Exercise;
import im.fitdiary.fitdiaryserver.exercise.presentation.dto.CreateExerciseReq;
import im.fitdiary.fitdiaryserver.exercise.presentation.dto.ExerciseRes;
import im.fitdiary.fitdiaryserver.exercise.presentation.dto.UpdateExerciseReq;
import im.fitdiary.fitdiaryserver.exercise.service.ExerciseService;
import im.fitdiary.fitdiaryserver.security.argumentresolver.Auth;
import im.fitdiary.fitdiaryserver.security.argumentresolver.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/exercise")
@RestController
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Secured("ROLE_USER_ACCESS")
    @PostMapping
    public Response create(@Auth AuthToken authToken, @RequestBody @Valid CreateExerciseReq req) {
        Exercise exercise = exerciseService.create(req.toDto(authToken.getId()));
        return Response.success(new ExerciseRes(exercise));
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping("/{exerciseId}")
    public Response findById(@Auth AuthToken authToken, @PathVariable("exerciseId") Long exerciseId) {
        Exercise exercise = exerciseService.findById(exerciseId, authToken.getId());
        return Response.success(new ExerciseRes(exercise));
    }

    @Secured("ROLE_USER_ACCESS")
    @PutMapping("/{exerciseId}")
    public Response updateById(
            @Auth AuthToken authToken,
            @PathVariable("exerciseId") Long exerciseId,
            @RequestBody @Valid UpdateExerciseReq req
    ) {
        exerciseService.updateById(exerciseId, authToken.getId(), req.toEditor());
        return Response.success();
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/{exerciseId}")
    public Response deleteById(@Auth AuthToken authToken, @PathVariable("exerciseId") Long exerciseId) {
        exerciseService.deleteById(exerciseId, authToken.getId());
        return Response.success();
    }
}
