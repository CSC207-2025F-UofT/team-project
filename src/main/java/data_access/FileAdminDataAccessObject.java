package data_access;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import entity.Admin;
import use_case.PasswordHasher;
import use_case.admin_login.AdminLoginDataAccessInterface;

/**
 * Stores the admin's hashed password in a text file.
 * The first time the program runs, the file is created from a default password,
 * so the real password never has to be written in the source code.
 */
public class FileAdminDataAccessObject implements AdminLoginDataAccessInterface {
    private final Admin admin;

    public FileAdminDataAccessObject(String filePath, String defaultPassword, PasswordHasher hasher) {
        final Path path = Path.of(filePath);
        try {
            if (!Files.exists(path)) {
                Files.writeString(path, hasher.hash(defaultPassword), StandardCharsets.UTF_8);
            }
            admin = new Admin(Files.readString(path, StandardCharsets.UTF_8).trim());
        }
        catch (IOException e) {
            throw new UncheckedIOException("Could not read or create " + path, e);
        }
    }

    @Override
    public Admin getAdmin() {
        return admin;
    }
}
