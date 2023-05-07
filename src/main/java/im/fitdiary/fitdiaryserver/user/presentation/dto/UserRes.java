package im.fitdiary.fitdiaryserver.user.presentation.dto;

import im.fitdiary.fitdiaryserver.user.data.entity.User;
import lombok.Getter;

@Getter
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
