package goc.chat.usecase.ports;

import goc.chat.entity.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    User save(User user);
}
