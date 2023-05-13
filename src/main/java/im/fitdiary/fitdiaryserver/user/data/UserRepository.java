package im.fitdiary.fitdiaryserver.user.data;

import im.fitdiary.fitdiaryserver.user.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}