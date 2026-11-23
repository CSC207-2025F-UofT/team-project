import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * Checks the Clean Architecture import rules by reading the source files:
 * entity and use_case must not depend on Swing, SQL, JSON, HTTP, or the outer layers.
 */
class ArchitectureImportTest {
    private static final Path SRC = Path.of("src", "main", "java");

    private static final List<String> FRAMEWORKS = List.of(
            "javax.swing", "java.awt", "java.sql", "org.json", "java.net.http", "org.sqlite");

    private static final List<String> OUTER_LAYERS = List.of(
            "data_access", "interface_adapter", "view", "app");

    @Test
    void entitiesOnlyUsePlainJava() throws IOException {
        List<String> forbidden = new ArrayList<>(FRAMEWORKS);
        forbidden.addAll(OUTER_LAYERS);
        forbidden.add("use_case");
        assertNoForbiddenImports("entity", forbidden);
    }

    @Test
    void useCasesDontUseFrameworksOrOuterLayers() throws IOException {
        List<String> forbidden = new ArrayList<>(FRAMEWORKS);
        forbidden.addAll(OUTER_LAYERS);
        assertNoForbiddenImports("use_case", forbidden);
    }

    private static void assertNoForbiddenImports(String layer, List<String> forbidden) throws IOException {
        List<String> violations = new ArrayList<>();
        try (Stream<Path> files = Files.walk(SRC.resolve(layer))) {
            for (Path file : files.filter(p -> p.toString().endsWith(".java")).collect(Collectors.toList())) {
                for (String line : Files.readAllLines(file)) {
                    String trimmed = line.trim();
                    if (!trimmed.startsWith("import ")) {
                        continue;
                    }
                    String imported = trimmed.substring("import ".length()).replace("static ", "");
                    for (String prefix : forbidden) {
                        if (imported.startsWith(prefix + ".")) {
                            violations.add(SRC.relativize(file) + ": " + trimmed);
                        }
                    }
                }
            }
        }
        assertTrue(violations.isEmpty(),
                layer + " has forbidden imports:\n" + String.join("\n", violations));
    }
}
