package im.fitdiary.fitdiaryserver.body.service.dto;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import lombok.Getter;

import java.util.List;

@Getter
public class BodyLogSlice {

    private final List<BodyLog> content;

    private final boolean hasNext;

    public BodyLogSlice(List<BodyLog> content, boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }
}
