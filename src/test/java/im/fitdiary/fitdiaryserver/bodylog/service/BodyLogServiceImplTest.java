package im.fitdiary.fitdiaryserver.bodylog.service;

import im.fitdiary.fitdiaryserver.bodylog.data.BodyLogRepository;
import im.fitdiary.fitdiaryserver.bodylog.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.bodylog.service.dto.CreateBodyLog;
import im.fitdiary.fitdiaryserver.common.converter.TimeConverter;
import im.fitdiary.fitdiaryserver.exception.e404.PreviousHeightNotFound;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.service.UserService;
import im.fitdiary.fitdiaryserver.util.factory.bodylog.BodyLogFactory;
import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

@ExtendWith(MockitoExtension.class)
class BodyLogServiceImplTest {

    @Mock
    BodyLogRepository bodyLogRepository;
    @Mock
    UserService userService;
    @InjectMocks
    BodyLogServiceImpl bodyLogService;

    @Nested
    @DisplayName("create")
    class Create {

        private Long userId;
        private CreateBodyLog createBodyLog;
        private User user;
        private BodyLog foundBodyLog;

        @BeforeEach
        void init() {
            userId = 1L;
            user = UserFactory.emailUser();
            createBodyLog = BodyLogFactory.createBodyLog();
            foundBodyLog = BodyLogFactory.bodyLog(user);
            given(userService.findById(userId))
                    .willReturn(user);
        }

        @Test
        @DisplayName("fail_withoutHeight_previousHeightNotFound")
        void fail_withoutHeight_previousHeightNotFound() {
            // given
            setField(createBodyLog, "height", null);
            given(bodyLogRepository.findLatestOne(user.getId()))
                    .willReturn(Optional.empty());

            // when - then
            assertThatThrownBy(() ->
                    bodyLogService.create(userId, createBodyLog)
            ).isInstanceOf(PreviousHeightNotFound.class);
        }

        @Test
        @DisplayName("success_withoutHeight")
        void success_withoutHeight() {
            // given
            setField(createBodyLog, "height", null);
            given(bodyLogRepository.findLatestOne(user.getId()))
                    .willReturn(Optional.of(foundBodyLog));

            // when
            BodyLog createdBodyLog = bodyLogService.create(userId, createBodyLog);

            // then
            assertThat(createdBodyLog.getUserId()).isEqualTo(user.getId());
            assertThat(createdBodyLog.getHeight()).isEqualTo(foundBodyLog.getHeight());
            assertThat(createdBodyLog.getWeight()).isEqualTo(createBodyLog.getWeight());
            assertThat(createdBodyLog.getMuscleMass()).isEqualTo(createBodyLog.getMuscleMass());
            assertThat(createdBodyLog.getBodyFat()).isEqualTo(createBodyLog.getBodyFat());
            assertThat(createdBodyLog.getMeasuredAt())
                    .isEqualTo(TimeConverter.toLocalDateTime(createBodyLog.getMeasuredAt()));
        }

        @Test
        @DisplayName("success_withoutMuscleMass")
        void success_withoutMuscleMass() {
            // given
            setField(createBodyLog, "muscleMass", null);

            // when
            BodyLog createdBodyLog = bodyLogService.create(userId, createBodyLog);

            // then
            verify(bodyLogRepository, never()).findLatestOne(anyLong());
            assertThat(createdBodyLog.getUserId()).isEqualTo(user.getId());
            assertThat(createdBodyLog.getHeight()).isEqualTo(createBodyLog.getHeight());
            assertThat(createdBodyLog.getWeight()).isEqualTo(createBodyLog.getWeight());
            assertThat(createdBodyLog.getMuscleMass()).isNull();
            assertThat(createdBodyLog.getBodyFat()).isEqualTo(createBodyLog.getBodyFat());
            assertThat(createdBodyLog.getMeasuredAt())
                    .isEqualTo(TimeConverter.toLocalDateTime(createBodyLog.getMeasuredAt()));
        }

        @Test
        @DisplayName("success_withoutBodyFat")
        void success_withoutBodyFat() {
            // given
            setField(createBodyLog, "bodyFat", null);

            // when
            BodyLog createdBodyLog = bodyLogService.create(userId, createBodyLog);

            // then
            verify(bodyLogRepository, never()).findLatestOne(anyLong());
            assertThat(createdBodyLog.getUserId()).isEqualTo(user.getId());
            assertThat(createdBodyLog.getHeight()).isEqualTo(createBodyLog.getHeight());
            assertThat(createdBodyLog.getWeight()).isEqualTo(createBodyLog.getWeight());
            assertThat(createdBodyLog.getMuscleMass()).isEqualTo(createBodyLog.getMuscleMass());
            assertThat(createdBodyLog.getBodyFat()).isNull();
            assertThat(createdBodyLog.getMeasuredAt())
                    .isEqualTo(TimeConverter.toLocalDateTime(createBodyLog.getMeasuredAt()));
        }

        @Test
        @DisplayName("success_withoutMeasuredAt")
        void success_withoutMeasuredAt() {
            // given
            setField(createBodyLog, "measuredAt", null);

            // when
            BodyLog createdBodyLog = bodyLogService.create(userId, createBodyLog);

            // then
            verify(bodyLogRepository, never()).findLatestOne(anyLong());
            assertThat(createdBodyLog.getUserId()).isEqualTo(user.getId());
            assertThat(createdBodyLog.getHeight()).isEqualTo(createBodyLog.getHeight());
            assertThat(createdBodyLog.getWeight()).isEqualTo(createBodyLog.getWeight());
            assertThat(createdBodyLog.getMuscleMass()).isEqualTo(createBodyLog.getMuscleMass());
            assertThat(createdBodyLog.getBodyFat()).isEqualTo(createBodyLog.getBodyFat());
            assertThat(createdBodyLog.getMeasuredAt()).isNull();
        }

        @Test
        @DisplayName("success")
        void success() {
            // when
            BodyLog createdBodyLog = bodyLogService.create(userId, createBodyLog);

            // then
            verify(bodyLogRepository, never()).findLatestOne(anyLong());
            assertThat(createdBodyLog.getUserId()).isEqualTo(user.getId());
            assertThat(createdBodyLog.getHeight()).isEqualTo(createBodyLog.getHeight());
            assertThat(createdBodyLog.getWeight()).isEqualTo(createBodyLog.getWeight());
            assertThat(createdBodyLog.getMuscleMass()).isEqualTo(createBodyLog.getMuscleMass());
            assertThat(createdBodyLog.getBodyFat()).isEqualTo(createBodyLog.getBodyFat());
            assertThat(createdBodyLog.getMeasuredAt())
                    .isEqualTo(TimeConverter.toLocalDateTime(createBodyLog.getMeasuredAt()));
        }
    }
}