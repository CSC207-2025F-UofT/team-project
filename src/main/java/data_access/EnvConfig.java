package data_access;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads configuration values such as API keys.
 *
 * <p>A value is taken from the real environment variable if it is set, otherwise from the
 * {@code .env} file in the working directory (the project root when run from IntelliJ).
 * Each line of {@code .env} has the form {@code NAME=value}; blank lines and lines starting
 * with {@code #} are ignored.</p>
 */
public final class EnvConfig {

    private static final String ENV_FILE = ".env";
    private static Map<String, String> fileValues;

    private EnvConfig() {
    }

    /**
     * Returns the value for the given name.
     * @param name the variable name, e.g. "ADZUNA_APP_KEY"
     * @return the value, or null if it is not set anywhere (or is blank)
     */
    public static String get(String name) {
        final String fromEnvironment = System.getenv(name);
        if (fromEnvironment != null && !fromEnvironment.isBlank()) {
            return fromEnvironment.trim();
        }
        final String fromFile = loadFile().get(name);
        if (fromFile == null || fromFile.isBlank()) {
            return null;
        }
        return fromFile;
    }

    private static synchronized Map<String, String> loadFile() {
        if (fileValues != null) {
            return fileValues;
        }
        fileValues = new HashMap<>();
        final Path path = Paths.get(ENV_FILE);
        if (!Files.isRegularFile(path)) {
            return fileValues;
        }
        try {
            final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String rawLine : lines) {
                final String line = rawLine.trim();
                final int equals = line.indexOf('=');
                if (line.isEmpty() || line.startsWith("#") || equals <= 0) {
                    continue;
                }
                final String key = line.substring(0, equals).trim();
                fileValues.put(key, unquote(line.substring(equals + 1).trim()));
            }
        }
        catch (IOException exception) {
            System.err.println("Could not read " + ENV_FILE + ": " + exception.getMessage());
        }
        return fileValues;
    }

    private static String unquote(String value) {
        if (value.length() >= 2
                && (value.startsWith("\"") && value.endsWith("\"")
                    || value.startsWith("'") && value.endsWith("'"))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
