package org.example.cocktailcraft.architecture;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

/** Basic source import guard; reviewers must still assess CA beyond imports. */
class DependencyRuleTest {
    @Test
    void innerLayersDoNotImportOuterLayers() throws Exception {
        Path root = Paths.get("src/main/java/org/example/cocktailcraft");
        for (String layer : List.of("entity", "use_case")) {
            try (var paths = Files.walk(root.resolve(layer))) {
                for (Path path : (Iterable<Path>) paths.filter(p -> p.toString().endsWith(".java"))::iterator) {
                    String source = Files.readString(path);
                    for (String forbidden : List.of("javax.swing", "java.awt", "com.google.gson",
                            "org.example.cocktailcraft.view", "org.example.cocktailcraft.interface_adapter",
                            "org.example.cocktailcraft.data_access", "org.example.cocktailcraft.app")) {
                        assertFalse(source.contains("import " + forbidden), path + " imports " + forbidden);
                    }
                    if (layer.equals("entity")) {
                        assertFalse(source.contains("import org.example.cocktailcraft.use_case"), path.toString());
                    }
                }
            }
        }
    }
}
