package im.fitdiary.server.common.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class TimeConverterTest {

    @Test
    @DisplayName("convert")
    void convert() {
        // given
        Long timestamp = 1683707949L;

        // when
        LocalDateTime localDateTime = TimeConverter.toLocalDateTime(timestamp);
        Long converted = TimeConverter.toTimestamp(localDateTime);

        // then
        assertThat(converted).isEqualTo(timestamp);
    }
}