package im.fitdiary.fitdiaryserver.user.data.dto;

import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class UserEditor {

    private final JsonNullable<String> name;

    public UserEditor(JsonNullable<String> name) {
        this.name = name;
    }
}
