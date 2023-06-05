package im.fitdiary.server.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response <T> {

    private final boolean success;

    private final T data;

    public Response(T data) {
        this.success = true;
        this.data = data;
    }

    public Response() {
        this.success = true;
        this.data = null;
    }
}
