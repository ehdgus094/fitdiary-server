package im.fitdiary.server.user.service;

import im.fitdiary.server.exception.e404.UserNotFoundException;
import im.fitdiary.server.exception.e409.AuthUserDuplicatedException;
import im.fitdiary.server.user.data.entity.User;
import im.fitdiary.server.user.service.dto.CreateUser;
import im.fitdiary.server.user.data.dto.UserEditor;

public interface UserService {

    User create(CreateUser createUser) throws AuthUserDuplicatedException;

    User findById(Long userId) throws UserNotFoundException;

    void updateById(Long userId, UserEditor userEditor) throws UserNotFoundException;

    void deleteById(Long userId);
}
