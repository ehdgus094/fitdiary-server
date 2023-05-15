package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.auth.service.AuthUserService;
import im.fitdiary.fitdiaryserver.exception.e404.UserNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e409.AuthUserDuplicatedException;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.data.UserRepository;
import im.fitdiary.fitdiaryserver.user.service.dto.CreateUser;
import im.fitdiary.fitdiaryserver.user.data.entity.UserEditor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void update(Long userId, UserEditor userEditor) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        userEditor.edit(user);
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            userRepository.delete(user);
            authUserService.delete(userId);
        });
    }
}
