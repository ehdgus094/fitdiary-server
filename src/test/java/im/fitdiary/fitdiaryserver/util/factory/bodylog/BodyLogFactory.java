package im.fitdiary.fitdiaryserver.util.factory.bodylog;

import im.fitdiary.fitdiaryserver.bodylog.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.bodylog.presentation.dto.CreateBodyLogReq;
import im.fitdiary.fitdiaryserver.bodylog.service.dto.CreateBodyLog;
import im.fitdiary.fitdiaryserver.user.data.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.util.ReflectionTestUtils.*;

public class BodyLogFactory {

    private static final BigDecimal HEIGHT = new BigDecimal("180.32");
    private static final BigDecimal WEIGHT = new BigDecimal("80.12");
    private static final BigDecimal MUSCLE_MASS = new BigDecimal("40.23");
    private static final BigDecimal BODY_FAT = new BigDecimal("13.56");
    private static final LocalDateTime MEASURED_AT = LocalDateTime.now();
    private static final Long MEASURED_AT_TIMESTAMP = 1683707949L;

    public static BodyLog bodyLog(User user) {
        return BodyLog.create(
                user.getId(),
                HEIGHT,
                WEIGHT,
                MUSCLE_MASS,
                BODY_FAT,
                MEASURED_AT
        );
    }

    public static CreateBodyLog createBodyLog() {
        return new CreateBodyLog(HEIGHT, WEIGHT, MUSCLE_MASS, BODY_FAT, MEASURED_AT_TIMESTAMP);
    }

    public static CreateBodyLogReq createBodyLogReq() {
        CreateBodyLogReq req = new CreateBodyLogReq();
        setField(req, "height", HEIGHT);
        setField(req, "weight", WEIGHT);
        setField(req, "muscleMass", MUSCLE_MASS);
        setField(req, "bodyFat", BODY_FAT);
        setField(req, "measuredAt", MEASURED_AT_TIMESTAMP);
        return req;
    }
}
