package im.fitdiary.server.exercise.presentation;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.common.dto.Response;
import im.fitdiary.server.exception.e404.ExerciseLogDetailNotFoundException;
import im.fitdiary.server.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.server.exception.e404.ExerciseNotFoundException;
import im.fitdiary.server.exercise.data.entity.ExerciseLogDetail;
import im.fitdiary.server.exercise.presentation.dto.*;
import im.fitdiary.server.exercise.service.ExerciseLogDetailService;
import im.fitdiary.server.exercise.service.dto.ExerciseLogDetailSlice;
import im.fitdiary.server.security.argumentresolver.Auth;
import im.fitdiary.server.security.argumentresolver.AuthToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Tag(name = "Exercise - LogDetail")
@BaseMethodLogging
@RequiredArgsConstructor
@RequestMapping("/exercise/log-details")
@RestController
public class ExerciseLogDetailController {

    private final ExerciseLogDetailService exerciseLogDetailService;

    @Secured("ROLE_USER_ACCESS")
    @PostMapping
    public Response<?> createBulk(
            @Auth AuthToken authToken,
            @RequestBody @Valid CreateExerciseLogDetailListReq req
    ) throws ExerciseLogNotFoundException, ExerciseNotFoundException {
        exerciseLogDetailService.createBulk(
                req.getExerciseLogId(),
                authToken.getId(),
                req.getData().stream()
                        .map(CreateExerciseLogDetailReq::toDto)
                        .collect(Collectors.toList())
        );
        return new Response<>();
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping("/{exerciseLogDetailId}")
    public Response<ExerciseLogDetailRes> findById(
            @Auth AuthToken authToken,
            @PathVariable("exerciseLogDetailId") Long exerciseLogDetailId
    ) throws ExerciseLogDetailNotFoundException {
        ExerciseLogDetail detail = exerciseLogDetailService.findById(exerciseLogDetailId, authToken.getId());
        return new Response<>(new ExerciseLogDetailRes(detail));
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping
    public Response<ExerciseLogDetailSliceRes> find(
            @Auth AuthToken authToken,
            Long exerciseLogId,
            Pageable pageable
    ) throws ExerciseLogNotFoundException {
        ExerciseLogDetailSlice exerciseLogDetailSlice =
                exerciseLogDetailService.find(pageable, exerciseLogId, authToken.getId());
        return new Response<>(new ExerciseLogDetailSliceRes(exerciseLogDetailSlice));
    }

    @Secured("ROLE_USER_ACCESS")
    @PutMapping
    public Response<?> updateBulk(
            @Auth AuthToken authToken,
            @RequestBody @Valid UpdateExerciseLogDetailListReq req
    ) throws ExerciseLogNotFoundException {
        exerciseLogDetailService.updateBulk(
                req.getExerciseLogId(),
                authToken.getId(),
                req.getData().stream()
                        .collect(Collectors.toMap(
                                UpdateExerciseLogDetailReq::getExerciseLogDetailId,
                                UpdateExerciseLogDetailReq::toEditor)
                        )
        );
        return new Response<>();
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/{exerciseLogDetailId}")
    public Response<?> deleteById(
            @Auth AuthToken authToken,
            @PathVariable("exerciseLogDetailId") Long exerciseLogDetailId
    ) {
        exerciseLogDetailService.deleteById(exerciseLogDetailId, authToken.getId());
        return new Response<>();
    }
}
