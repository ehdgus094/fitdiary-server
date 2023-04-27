package im.fitdiary.fitdiaryserver.user.dto;

import im.fitdiary.fitdiaryserver.user.entity.User;
import lombok.Getter;

@Getter
public class UserRes {

    private final String name;
    private final String birthYmd;
    private final Integer height;
    private final Integer weight;

    public UserRes(User user) {
        this.name = user.getName();
        this.birthYmd = user.getBirthYmd();
        this.height = user.getHeight();
        this.weight = user.getWeight();
    }
}
