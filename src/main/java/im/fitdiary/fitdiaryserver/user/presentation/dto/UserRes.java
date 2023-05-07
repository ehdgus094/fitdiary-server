package im.fitdiary.fitdiaryserver.user.presentation.dto;

import im.fitdiary.fitdiaryserver.user.data.entity.User;
import lombok.Getter;

@Getter
public class UserRes {

    private final String name;
    private final String birthYmd;
    private final String email;

    public UserRes(User user) {
        this.name = user.getName();
        this.birthYmd = user.getBirthYmd();
        this.email = user.getEmail();
    }
}
