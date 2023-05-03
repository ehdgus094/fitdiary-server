package im.fitdiary.fitdiaryserver.user.dto;

import im.fitdiary.fitdiaryserver.user.entity.User;
import lombok.Getter;

@Getter
public class UserRes {

    private final String name;
    private final String birthYmd;

    public UserRes(User user) {
        this.name = user.getName();
        this.birthYmd = user.getBirthYmd();
    }
}
