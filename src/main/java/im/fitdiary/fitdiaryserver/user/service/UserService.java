package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.user.dto.LoginUserRes;
import im.fitdiary.fitdiaryserver.user.dto.UserRes;
import im.fitdiary.fitdiaryserver.user.entity.User;

public interface UserService {
    void create(User user);
    LoginUserRes login(String loginId, String password);
    UserRes findById(Long id);
    void deleteById(Long id);
}
