package im.fitdiary.fitdiaryserver.user.presentation.dto;

import im.fitdiary.fitdiaryserver.user.data.entity.UserEditor;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotBlank;

@Getter
public class UpdateUserReq {

    @NotBlank(message = "name should not be empty")
    private JsonNullable<String> name = JsonNullable.undefined();

    public UserEditor toEditor() {
        return new UserEditor(name);
    }
}