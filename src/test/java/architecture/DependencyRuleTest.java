package architecture;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class DependencyRuleTest {
  private static final Pattern IMPORT = Pattern.compile("(?m)^import\\s+(?:static\\s+)?([\\w.]+);");

  @Test
  void entitiesAndUseCasesDoNotImportAdaptersUiIoOrExternalLibraries() throws IOException {
    for (String layer : List.of("entity", "use_case")) {
      try (Stream<Path> paths = Files.walk(Path.of("src/main/java", layer))) {
        List<Path> sources =
            paths.filter(path -> path.toString().endsWith(".java")).collect(Collectors.toList());
        for (Path source : sources) {
          Matcher imports = IMPORT.matcher(Files.readString(source));
          while (imports.find()) {
            String dependency = imports.group(1);
            boolean allowed =
                dependency.startsWith("java.lang.")
                    || dependency.startsWith("java.util.")
                    || dependency.startsWith("entity.")
                    || ("use_case".equals(layer) && dependency.startsWith("use_case."));
            assertTrue(allowed, source + " imports outer-layer dependency " + dependency);
          }
        }
      }
    }
  }
}
