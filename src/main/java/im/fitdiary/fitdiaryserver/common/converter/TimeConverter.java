package im.fitdiary.fitdiaryserver.common.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeConverter {

    public static LocalDateTime toLocalDateTime(Long timestampInSeconds) {
        if (timestampInSeconds == null) return null;
        return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestampInSeconds),
                TimeZone.getDefault().toZoneId()
        );
    }

    public static Long toTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        ZoneId zoneId = ZoneId.systemDefault();
        return dateTime.atZone(zoneId).toEpochSecond();
    }
}
