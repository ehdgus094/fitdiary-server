package im.fitdiary.fitdiaryserver.user.data.dto;

import lombok.Getter;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@ToString
public class UserEditor {

    private final JsonNullable<String> name;

    public UserEditor(JsonNullable<String> name) {
        this.name = name;
    }
}
