package im.fitdiary.fitdiaryserver.bodylog.service.dto;

import im.fitdiary.fitdiaryserver.bodylog.data.entity.BodyLog;
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
