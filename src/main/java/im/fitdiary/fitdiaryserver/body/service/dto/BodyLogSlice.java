package im.fitdiary.fitdiaryserver.body.service.dto;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class BodyLogSlice {

    private final List<BodyLog> content;

    private final boolean hasNext;

    public BodyLogSlice(List<BodyLog> content, boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }
}
