package im.fitdiary.fitdiaryserver.util.factory.body;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.body.data.entity.BodyLogEditor;
import im.fitdiary.fitdiaryserver.body.presentation.dto.CreateBodyLogReq;
import im.fitdiary.fitdiaryserver.body.presentation.dto.UpdateBodyLogReq;
import im.fitdiary.fitdiaryserver.body.service.dto.BodyLogSlice;
import im.fitdiary.fitdiaryserver.body.service.dto.CreateBodyLog;
import org.openapitools.jackson.nullable.JsonNullable;

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

    public static BodyLogEditor bodyLogEditor() {
        return new BodyLogEditor(
                JsonNullable.of(HEIGHT),
                JsonNullable.of(WEIGHT),
                JsonNullable.of(MUSCLE_MASS),
                JsonNullable.of(BODY_FAT),
                JsonNullable.of(MEASURED_AT)
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

    public static UpdateBodyLogReq updateBodyLogReq() {
        UpdateBodyLogReq req = new UpdateBodyLogReq();
        setField(req, "height", JsonNullable.of(HEIGHT));
        setField(req, "weight", JsonNullable.of(WEIGHT));
        setField(req, "muscleMass", JsonNullable.of(MUSCLE_MASS));
        setField(req, "bodyFat", JsonNullable.of(BODY_FAT));
        setField(req, "measuredAt", JsonNullable.of(MEASURED_AT_TIMESTAMP));
        return req;
    }
}
