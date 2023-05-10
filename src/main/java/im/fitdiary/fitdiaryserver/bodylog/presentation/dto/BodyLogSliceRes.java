package im.fitdiary.fitdiaryserver.bodylog.presentation.dto;

import im.fitdiary.fitdiaryserver.bodylog.service.dto.BodyLogSlice;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BodyLogSliceRes {

    private final List<BodyLogRes> content;
    private final boolean hasNext;

    public BodyLogSliceRes(BodyLogSlice bodyLogSlice) {
        this.content = bodyLogSlice
                .getContent()
                .stream()
                .map(BodyLogRes::new)
                .collect(Collectors.toList());
        this.hasNext = bodyLogSlice.isHasNext();
    }
}
