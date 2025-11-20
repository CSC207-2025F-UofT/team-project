package use_case.ports;

import entity.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User save(User user);
}
