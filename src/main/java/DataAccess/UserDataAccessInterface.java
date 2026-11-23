package DataAccess;

import Entities.User;
import java.sql.SQLException;
import java.util.Optional;


public interface UserDataAccessInterface {
    void create(User user) throws SQLException;

    Optional<User> findByUsername(String username) throws SQLException;
}
