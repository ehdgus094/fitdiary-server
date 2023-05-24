package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.auth.service.AuthUserService;
import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.fitdiaryserver.exception.e404.UserNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e409.AuthUserDuplicatedException;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.data.UserRepository;
import im.fitdiary.fitdiaryserver.user.service.dto.CreateUser;
import im.fitdiary.fitdiaryserver.user.data.dto.UserEditor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@BaseMethodLogging
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AuthUserService authUserService;

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
            authUserService.deleteByUserId(userId);
        });
    }
}
