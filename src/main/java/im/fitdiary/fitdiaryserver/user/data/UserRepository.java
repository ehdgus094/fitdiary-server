package im.fitdiary.fitdiaryserver.user.data;

import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

@BaseMethodLogging
public interface UserRepository extends JpaRepository<User, Long> {
}