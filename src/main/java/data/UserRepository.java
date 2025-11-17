package data;

import entities.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    User create(String username, String password);
}
