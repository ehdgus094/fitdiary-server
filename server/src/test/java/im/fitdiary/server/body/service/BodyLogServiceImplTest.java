package im.fitdiary.server.body.service;

import im.fitdiary.server.body.data.BodyLogRepository;
import im.fitdiary.server.body.data.entity.BodyLog;
import im.fitdiary.server.body.service.dto.CreateBodyLog;
import im.fitdiary.server.exception.e404.PreviousHeightNotFound;
import im.fitdiary.server.util.factory.body.BodyFactory;
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

    @InjectMocks
    BodyLogServiceImpl bodyLogService;

    @Nested
    @DisplayName("create")
    class Create {

        CreateBodyLog createBodyLog;

        BodyLog foundBodyLog;

        @BeforeEach
        void init() {
            createBodyLog = BodyFactory.createBodyLog();
            foundBodyLog = BodyFactory.bodyLog();
        }

        @Test
        @DisplayName("fail_withoutHeight_previousHeightNotFound")
        void fail_withoutHeight_previousHeightNotFound() {
            // given
            setField(createBodyLog, "height", null);
            given(bodyLogRepository.findLatestOne(createBodyLog.getUserId()))
                    .willReturn(Optional.empty());

            // when - then
            assertThatThrownBy(() ->
                    bodyLogService.create(createBodyLog)
            ).isInstanceOf(PreviousHeightNotFound.class);
        }

        @Test
        @DisplayName("success_withoutHeight")
        void success_withoutHeight() {
            // given
            setField(createBodyLog, "height", null);
            given(bodyLogRepository.findLatestOne(createBodyLog.getUserId()))
                    .willReturn(Optional.of(foundBodyLog));

            // when
            BodyLog createdBodyLog = bodyLogService.create(createBodyLog);

            // then
            assertThat(createdBodyLog.getUserId()).isEqualTo(createBodyLog.getUserId());
            assertThat(createdBodyLog.getHeight()).isEqualTo(foundBodyLog.getHeight());
            assertThat(createdBodyLog.getWeight()).isEqualTo(createBodyLog.getWeight());
            assertThat(createdBodyLog.getMuscleMass()).isEqualTo(createBodyLog.getMuscleMass());
            assertThat(createdBodyLog.getBodyFat()).isEqualTo(createBodyLog.getBodyFat());
            assertThat(createdBodyLog.getMeasuredAt()).isEqualTo(createBodyLog.getMeasuredAt());
        }

        @Test
        @DisplayName("success_withoutMuscleMass")
        void success_withoutMuscleMass() {
            // given
            setField(createBodyLog, "muscleMass", null);

            // when
            BodyLog createdBodyLog = bodyLogService.create(createBodyLog);

            // then
            verify(bodyLogRepository, never()).findLatestOne(any());
            assertThat(createdBodyLog.getUserId()).isEqualTo(createBodyLog.getUserId());
            assertThat(createdBodyLog.getHeight()).isEqualTo(createBodyLog.getHeight());
            assertThat(createdBodyLog.getWeight()).isEqualTo(createBodyLog.getWeight());
            assertThat(createdBodyLog.getMuscleMass()).isNull();
            assertThat(createdBodyLog.getBodyFat()).isEqualTo(createBodyLog.getBodyFat());
            assertThat(createdBodyLog.getMeasuredAt()).isEqualTo(createBodyLog.getMeasuredAt());
        }

        @Test
        @DisplayName("success_withoutBodyFat")
        void success_withoutBodyFat() {
            // given
            setField(createBodyLog, "bodyFat", null);

            // when
            BodyLog createdBodyLog = bodyLogService.create(createBodyLog);

            // then
            verify(bodyLogRepository, never()).findLatestOne(any());
            assertThat(createdBodyLog.getUserId()).isEqualTo(createBodyLog.getUserId());
            assertThat(createdBodyLog.getHeight()).isEqualTo(createBodyLog.getHeight());
            assertThat(createdBodyLog.getWeight()).isEqualTo(createBodyLog.getWeight());
            assertThat(createdBodyLog.getMuscleMass()).isEqualTo(createBodyLog.getMuscleMass());
            assertThat(createdBodyLog.getBodyFat()).isNull();
            assertThat(createdBodyLog.getMeasuredAt()).isEqualTo(createBodyLog.getMeasuredAt());
        }

        @Test
        @DisplayName("success_withoutMeasuredAt")
        void success_withoutMeasuredAt() {
            // given
            setField(createBodyLog, "measuredAt", null);

            // when
            BodyLog createdBodyLog = bodyLogService.create(createBodyLog);

            // then
            verify(bodyLogRepository, never()).findLatestOne(any());
            assertThat(createdBodyLog.getUserId()).isEqualTo(createBodyLog.getUserId());
            assertThat(createdBodyLog.getHeight()).isEqualTo(createBodyLog.getHeight());
            assertThat(createdBodyLog.getWeight()).isEqualTo(createBodyLog.getWeight());
            assertThat(createdBodyLog.getMuscleMass()).isEqualTo(createBodyLog.getMuscleMass());
            assertThat(createdBodyLog.getBodyFat()).isEqualTo(createBodyLog.getBodyFat());
            assertThat(createdBodyLog.getMeasuredAt()).isNotNull();
        }

        @Test
        @DisplayName("success")
        void success() {
            // when
            BodyLog createdBodyLog = bodyLogService.create(createBodyLog);

            // then
            verify(bodyLogRepository, never()).findLatestOne(any());
            assertThat(createdBodyLog.getUserId()).isEqualTo(createBodyLog.getUserId());
            assertThat(createdBodyLog.getHeight()).isEqualTo(createBodyLog.getHeight());
            assertThat(createdBodyLog.getWeight()).isEqualTo(createBodyLog.getWeight());
            assertThat(createdBodyLog.getMuscleMass()).isEqualTo(createBodyLog.getMuscleMass());
            assertThat(createdBodyLog.getBodyFat()).isEqualTo(createBodyLog.getBodyFat());
            assertThat(createdBodyLog.getMeasuredAt()).isEqualTo(createBodyLog.getMeasuredAt());
        }
    }
}