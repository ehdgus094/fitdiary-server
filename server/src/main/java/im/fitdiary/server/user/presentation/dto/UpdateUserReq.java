package im.fitdiary.server.user.presentation.dto;

import im.fitdiary.server.user.data.dto.UserEditor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@Schema
public class UpdateUserReq {

    @NotBlank
    private JsonNullable<String> name = JsonNullable.undefined();

    public UserEditor toEditor() {
        return new UserEditor(name);
    }
}
