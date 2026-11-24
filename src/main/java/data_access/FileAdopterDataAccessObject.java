package data_access;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import entity.Adopter;
import use_case.adopter_login.AdopterLoginDataAccessInterface;
import use_case.adopter_signup.AdopterSignupDataAccessInterface;

/**
 * Stores adopter accounts in a CSV file: "username,passwordHash".
 * Accounts are loaded when the program starts and saved every time one is added,
 * so they are still there after the program is restarted.
 */
public class FileAdopterDataAccessObject implements AdopterSignupDataAccessInterface,
        AdopterLoginDataAccessInterface {
    private static final String HEADER = "username,passwordHash";

    private final Path csvPath;
    private final Map<String, Adopter> accounts = new LinkedHashMap<>();

    public FileAdopterDataAccessObject(String csvPath) {
        this.csvPath = Path.of(csvPath);
        if (Files.exists(this.csvPath)) {
            load();
        }
        else {
            writeAll();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return accounts.containsKey(username);
    }

    @Override
    public Adopter get(String username) {
        return accounts.get(username);
    }

    @Override
    public void save(Adopter adopter) {
        accounts.put(adopter.getUsername(), adopter);
        writeAll();
    }

    private void load() {
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            if (line != null && !HEADER.equals(line)) {
                throw new IllegalStateException("Unexpected header in " + csvPath + ": " + line);
            }
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                final String[] parts = line.split(",", 2);
                accounts.put(parts[0], new Adopter(parts[0], parts[1]));
            }
        }
        catch (IOException e) {
            throw new UncheckedIOException("Could not read " + csvPath, e);
        }
    }

    private void writeAll() {
        try (BufferedWriter writer = Files.newBufferedWriter(csvPath, StandardCharsets.UTF_8)) {
            writer.write(HEADER);
            writer.newLine();
            for (Adopter adopter : accounts.values()) {
                writer.write(adopter.getUsername() + "," + adopter.getPasswordHash());
                writer.newLine();
            }
        }
        catch (IOException e) {
            throw new UncheckedIOException("Could not write " + csvPath, e);
        }
    }
}
