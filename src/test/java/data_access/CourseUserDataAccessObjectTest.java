
package data_access;

import entity.CommonUserFactory;
import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CourseUserDataAccessObjectTest {

    @Test
    public void testSaveAndGetUser() {

        UserFactory factory = new CommonUserFactory();

        CourseUserDataAccessObject dao =
                new CourseUserDataAccessObject(factory);

        String username = "stocktest_" +
                UUID.randomUUID().toString().substring(0, 8);

        String password = "Test123456";

        // 1. Check that the user does not exist
        assertFalse(dao.existsByName(username));

        // 2. Create and save a new user
        User user = factory.create(username, password);
        dao.save(user);

        // 3. Check that the user now exists
        assertTrue(dao.existsByName(username));

        // 4. Retrieve the user from the API
        User savedUser = dao.get(username);

        assertEquals(username, savedUser.getName());
        assertEquals(password, savedUser.getPassword());

        // 5. Check current username
        dao.setCurrentUsername(username);
        assertEquals(username, dao.getCurrentUsername());
    }
}
