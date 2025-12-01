package data_access;

import entity.User;
import java.io.File;

/**
 * File-based implementation of UserDAO
 * Saves user data to userdata.json
 */
public class FileUserDAO implements UserDAO {
    private final FileDataAccess fileDataAccess;
    private static final String FILE_PATH = "userdata.json";

    public FileUserDAO() {
        this.fileDataAccess = new FileDataAccess();
    }

    @Override
    public void saveUser(User user) {
        fileDataAccess.save(user);
    }

    @Override
    public User loadUser() {
        return fileDataAccess.load(User.class);
    }

    @Override
    public boolean userExists() {
        File file = new File(FILE_PATH);
        return file.exists() && file.length() > 0;
    }
}
