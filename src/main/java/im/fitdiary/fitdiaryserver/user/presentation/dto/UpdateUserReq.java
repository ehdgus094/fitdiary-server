package im.fitdiary.fitdiaryserver.user.presentation.dto;

import im.fitdiary.fitdiaryserver.user.data.dto.UserEditor;
import lombok.Getter;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class UpdateUserReq {

    @NotBlank(message = "name should not be empty")
    private JsonNullable<String> name = JsonNullable.undefined();

    public UserEditor toEditor() {
        return new UserEditor(name);
    }
}
