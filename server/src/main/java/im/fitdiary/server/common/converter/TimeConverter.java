package im.fitdiary.server.common.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeConverter {

    public static LocalDateTime toLocalDateTime(Long timestampInSeconds) {
        if (timestampInSeconds == null) return null;
        LocalDateTime result = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestampInSeconds),
                TimeZone.getDefault().toZoneId()
        );
        log.debug("toLocalDateTime call {} -> {}", timestampInSeconds, result);
        return result;
    }

    public static Long toTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        long result = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        log.debug("toTimestamp call {} -> {}", dateTime, result);
        return result;
    }
}
