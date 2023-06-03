package im.fitdiary.server.user.service;

import im.fitdiary.server.auth.service.AuthUserService;
import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.exception.e404.UserNotFoundException;
import im.fitdiary.server.exception.e409.AuthUserDuplicatedException;
import im.fitdiary.server.user.data.entity.User;
import im.fitdiary.server.user.data.UserRepository;
import im.fitdiary.server.user.event.UserProducer;
import im.fitdiary.server.user.service.dto.CreateUser;
import im.fitdiary.server.user.data.dto.UserEditor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@BaseMethodLogging
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AuthUserService authUserService;

    private final UserProducer userProducer;

    @Transactional
    public User create(CreateUser createUser) throws AuthUserDuplicatedException {
        User user = createUser.toUserEntity();
        userRepository.save(user);
        authUserService.create(createUser.toAuthUserServiceDto(user.getId()));
        return user;
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void updateById(Long userId, UserEditor editor) throws UserNotFoundException {
        User user = findById(userId);
        user.update(editor);
    }

    @Transactional
    public void deleteById(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            userRepository.delete(user);
            userProducer.userDeleted(user.getId());
        });
    }
}
