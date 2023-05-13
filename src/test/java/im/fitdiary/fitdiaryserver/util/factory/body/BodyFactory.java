package im.fitdiary.fitdiaryserver.util.factory.body;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.body.presentation.dto.CreateBodyLogReq;
import im.fitdiary.fitdiaryserver.body.service.dto.BodyLogSlice;
import im.fitdiary.fitdiaryserver.body.service.dto.CreateBodyLog;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.springframework.test.util.ReflectionTestUtils.*;

public class BodyFactory {

    private static final Long USER_ID = 1L;
    private static final BigDecimal HEIGHT = new BigDecimal("180.32");
    private static final BigDecimal WEIGHT = new BigDecimal("80.12");
    private static final BigDecimal MUSCLE_MASS = new BigDecimal("40.23");
    private static final BigDecimal BODY_FAT = new BigDecimal("13.56");
    private static final LocalDateTime MEASURED_AT = LocalDateTime.now();
    private static final Long MEASURED_AT_TIMESTAMP = 1683707949L;

    public static BodyLog bodyLog() {
        return BodyLog.create(
                USER_ID,
                HEIGHT,
                WEIGHT,
                MUSCLE_MASS,
                BODY_FAT,
                MEASURED_AT
        );
    }

    public static CreateBodyLog createBodyLog() {
        return new CreateBodyLog(USER_ID, HEIGHT, WEIGHT, MUSCLE_MASS, BODY_FAT, MEASURED_AT);
    }

    public static BodyLogSlice bodyLogSlice() {
        ArrayList<BodyLog> bodyLogs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            bodyLogs.add(bodyLog());
        }
        return new BodyLogSlice(bodyLogs, false);
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
