package auth.data;

import auth.entity.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    User create(String username, String password);
}
