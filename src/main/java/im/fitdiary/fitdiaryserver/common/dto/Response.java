package im.fitdiary.fitdiaryserver.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private final boolean success;

    private final String message;

    private final Object data;

    public static Response success() {
        return new Response(true, null, null);
    }

    public static Response success(Object data) {
        return new Response(true, null, data);
    }

    public static Response failure(String message) {
        return new Response(false, message, null);
    }

    private Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
