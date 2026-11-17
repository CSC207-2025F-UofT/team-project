package org.example.cocktailcraft.data_access;

import org.example.cocktailcraft.entity.Cocktail;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.net.URLClassLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CocktailJsonReaderTest {
    private final CocktailJsonReader reader = new CocktailJsonReader();

    @Test
    void loadsPinnedDatasetWithUniqueIds() throws IOException {
        List<Cocktail> cocktails = reader.readAll();
        assertEquals(663, cocktails.size());
        Set<String> ids = new HashSet<>();
        for (Cocktail cocktail : cocktails) {
            assertTrue(ids.add(cocktail.getId()), cocktail.getId());
            assertTrue(!cocktail.getName().isBlank());
            assertTrue(!cocktail.getIngredients().isEmpty());
        }
        assertThrows(UnsupportedOperationException.class, cocktails::clear);
    }

    @Test
    void mapsDaiquiriFieldsAndQuantities() throws IOException {
        Cocktail cocktail = reader.read("/bar-assistant/data/cocktails/daiquiri/data.json");
        assertEquals("daiquiri", cocktail.getId());
        assertEquals("Daiquiri", cocktail.getName());
        assertEquals(3, cocktail.getIngredients().size());
        assertEquals("Light Rum", cocktail.getIngredients().get(0).getName());
        assertEquals("60", cocktail.getIngredients().get(0).getAmount());
        assertEquals("ml", cocktail.getIngredients().get(0).getUnit());
        assertEquals("22.5", cocktail.getIngredients().get(1).getAmount());
    }

    @Test
    void indexContainsExactlyAllBundledRecipePaths() throws IOException {
        Path resources = Paths.get("src/main/resources");
        List<String> index = Files.readAllLines(resources.resolve("bar-assistant/cocktail-index.txt"));
        Set<String> actual = new HashSet<>();
        try (var paths = Files.walk(resources.resolve("bar-assistant/data/cocktails"))) {
            paths.filter(path -> path.getFileName().toString().equals("data.json"))
                    .forEach(path -> actual.add("/" + resources.relativize(path).toString().replace('\\', '/')));
        }
        assertEquals(actual, new HashSet<>(index));
        assertEquals(actual.size(), index.size());
    }

    @Test
    void reportsMissingAndInvalidResourcesWithPaths() {
        for (String path : List.of("/missing.json", "/fixtures/broken.json", "/fixtures/missing-fields.json")) {
            IOException error = assertThrows(IOException.class, () -> reader.read(path));
            assertTrue(error.getMessage().contains(path));
        }
        assertThrows(IOException.class, () -> reader.read("relative.json"));
    }

    @Test
    void refusesEmptyDuplicateOrMissingIndexEntries() {
        for (String path : List.of("/fixtures/empty.txt", "/fixtures/duplicate.txt", "/fixtures/missing.txt")) {
            assertThrows(IOException.class, () -> reader.readAll(path));
        }
    }

    @Test
    void readsRecipeAndIndexFromJarResources() throws Exception {
        Path jar = Files.createTempFile("cocktailcraft-resources-", ".jar");
        try {
            try (JarOutputStream out = new JarOutputStream(Files.newOutputStream(jar))) {
                out.putNextEntry(new JarEntry("jar-only/index.txt"));
                out.write("/jar-only/recipe.json\n".getBytes(java.nio.charset.StandardCharsets.UTF_8));
                out.closeEntry();
                out.putNextEntry(new JarEntry("jar-only/recipe.json"));
                try (InputStream recipe = getClass().getResourceAsStream("/cocktails/daiquiri.json")) {
                    assertNotNull(recipe);
                    recipe.transferTo(out);
                }
                out.closeEntry();
            }
            // Child loader isolates Reader so its classpath resource lookup includes this JAR.
            var classes = CocktailJsonReader.class.getProtectionDomain().getCodeSource().getLocation();
            var gson = com.google.gson.JsonParser.class.getProtectionDomain().getCodeSource().getLocation();
            try (URLClassLoader loader = new URLClassLoader(
                    new java.net.URL[] {jar.toUri().toURL(), classes, gson}, ClassLoader.getPlatformClassLoader())) {
                Class<?> type = loader.loadClass(CocktailJsonReader.class.getName());
                Object instance = type.getConstructor().newInstance();
                List<?> result = (List<?>) type.getMethod("readAll", String.class)
                        .invoke(instance, "/jar-only/index.txt");
                assertEquals(1, result.size());
                assertEquals("Daiquiri", result.get(0).getClass().getMethod("getName").invoke(result.get(0)));
            }
        } finally {
            Files.deleteIfExists(jar);
        }
    }
}
