package im.fitdiary.server.user.presentation.dto;

import im.fitdiary.server.user.data.entity.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserRes {

    private final String name;

    private final String birthYmd;

    private final String email;

    public UserRes(User user) {
        name = user.getName();
        birthYmd = user.getBirthYmd();
        email = user.getEmail();
    }
}
