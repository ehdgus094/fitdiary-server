package im.fitdiary.fitdiaryserver.exercise.presentation;

import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLogDetail;
import im.fitdiary.fitdiaryserver.exercise.presentation.dto.*;
import im.fitdiary.fitdiaryserver.exercise.service.ExerciseLogDetailService;
import im.fitdiary.fitdiaryserver.security.argumentresolver.Auth;
import im.fitdiary.fitdiaryserver.security.argumentresolver.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@BaseMethodLogging
@RequiredArgsConstructor
@RequestMapping("/exercise/log/detail")
@RestController
public class ExerciseLogDetailController {

    private final ExerciseLogDetailService exerciseLogDetailService;

    @Secured("ROLE_USER_ACCESS")
    @PostMapping
    public Response createBulk(
            @Auth AuthToken authToken,
            @RequestBody @Valid CreateExerciseLogDetailListReq req
    ) {
        exerciseLogDetailService.bulkInsert(
                req.getExerciseLogId(),
                authToken.getId(),
                req.getData().stream()
                        .map(CreateExerciseLogDetailReq::toDto)
                        .collect(Collectors.toList())
        );
        return Response.success();
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping("/{exerciseLogDetailId}")
    public Response findById(
            @Auth AuthToken authToken,
            @PathVariable("exerciseLogDetailId") Long exerciseLogDetailId
    ) {
        ExerciseLogDetail detail = exerciseLogDetailService.findById(exerciseLogDetailId, authToken.getId());
        return Response.success(new ExerciseLogDetailRes(detail));
    }

    @Secured("ROLE_USER_ACCESS")
    @PutMapping
    public Response updateBulk(
            @Auth AuthToken authToken,
            @RequestBody @Valid UpdateExerciseLogDetailListReq req
            ) {
        exerciseLogDetailService.bulkUpdate(
                req.getExerciseLogId(),
                authToken.getId(),
                req.getData().stream()
                        .collect(Collectors.toMap(
                                UpdateExerciseLogDetailReq::getExerciseLogDetailId,
                                UpdateExerciseLogDetailReq::toEditor)
                        )
        );
        return Response.success();
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/{exerciseLogDetailId}")
    public Response deleteById(
            @Auth AuthToken authToken,
            @PathVariable("exerciseLogDetailId") Long exerciseLogDetailId
    ) {
        exerciseLogDetailService.deleteById(exerciseLogDetailId, authToken.getId());
        return Response.success();
    }
}
