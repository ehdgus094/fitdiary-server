package im.fitdiary.fitdiaryserver.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private final boolean success;
    private final String message;
    private final Object data;

    public static Response success() {
        return Response.builder()
                .success(true)
                .build();
    }

    public static Response success(Object data) {
        return Response.builder()
                .success(true)
                .data(data)
                .build();
    }

    public static Response failure(String message) {
        return Response.builder()
                .success(false)
                .message(message)
                .build();
    }

    @Builder
    private Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
