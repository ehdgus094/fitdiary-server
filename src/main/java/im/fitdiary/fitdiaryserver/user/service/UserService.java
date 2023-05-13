package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.exception.e404.UserNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e409.AuthUserDuplicatedException;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.service.dto.CreateUser;

public interface UserService {
    User create(CreateUser createUser) throws AuthUserDuplicatedException;
    User findById(Long userId) throws UserNotFoundException;
    void delete(Long userId);
}
