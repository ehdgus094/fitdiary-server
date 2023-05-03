package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.exception.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.NotFoundException;
import im.fitdiary.fitdiaryserver.user.dto.LoginUserRes;
import im.fitdiary.fitdiaryserver.user.dto.UserRes;
import im.fitdiary.fitdiaryserver.user.entity.User;

import java.util.NoSuchElementException;

public interface UserService {
    void create(User user);
    LoginUserRes login(String loginId, String password) throws InvalidLoginInfoException, NoSuchElementException;
    UserRes findById(Long id) throws NotFoundException;
    void deleteById(Long id);
}
