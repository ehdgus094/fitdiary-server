package im.fitdiary.server.exception.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FailureResponse {

    private final boolean success;

    private final String message;

    public FailureResponse(String message) {
        this.success = false;
        this.message = message;
    }
}
