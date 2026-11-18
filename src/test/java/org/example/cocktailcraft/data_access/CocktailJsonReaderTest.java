package org.example.cocktailcraft.data_access;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.Tags.BaseSpirit;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
            assertNotNull(cocktail.getTags());
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
        assertEquals(Set.of(BaseSpirit.RUM), cocktail.getTags().getBaseSpirits());
    }

    @Test
    void mapsAllSevenFamiliesUsingIngredientAncestors() throws IOException {
        Map<String, BaseSpirit> examples = Map.of(
                "negroni", BaseSpirit.GIN,
                "moscow-mule", BaseSpirit.VODKA,
                "daiquiri", BaseSpirit.RUM,
                "margarita", BaseSpirit.TEQUILA,
                "old-fashioned", BaseSpirit.WHISKEY,
                "sidecar", BaseSpirit.BRANDY,
                "2-to-2", BaseSpirit.OTHER);
        for (Map.Entry<String, BaseSpirit> example : examples.entrySet()) {
            Cocktail cocktail = reader.read("/bar-assistant/data/cocktails/" + example.getKey() + "/data.json");
            assertEquals(Set.of(example.getValue()), cocktail.getTags().getBaseSpirits(), example.getKey());
        }
    }

    @Test
    void preservesMultipleFamiliesAndReturnsImmutableTags() throws IOException {
        Cocktail cocktail = reader.read("/bar-assistant/data/cocktails/long-island-ice-tea/data.json");
        Set<BaseSpirit> spirits = cocktail.getTags().getBaseSpirits();
        assertEquals(Set.of(BaseSpirit.GIN, BaseSpirit.VODKA, BaseSpirit.RUM, BaseSpirit.TEQUILA), spirits);
        assertThrows(UnsupportedOperationException.class, () -> spirits.add(BaseSpirit.OTHER));
    }

    @Test
    void doesNotTreatLiqueursAsOtherSpirits() throws IOException {
        Cocktail cocktail = reader.read("/bar-assistant/data/cocktails/8-amaro-sazerac/data.json");
        assertEquals(Set.of(), cocktail.getTags().getBaseSpirits());
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
                Path resources = Paths.get("src/main/resources");
                try (var paths = Files.walk(resources.resolve("bar-assistant/data/ingredients"))) {
                    for (Path path : (Iterable<Path>) paths.filter(Files::isRegularFile)::iterator) {
                        String entry = resources.relativize(path).toString().replace('\\', '/');
                        out.putNextEntry(new JarEntry(entry));
                        Files.copy(path, out);
                        out.closeEntry();
                    }
                }
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
                Object tags = result.get(0).getClass().getMethod("getTags").invoke(result.get(0));
                Set<?> spirits = (Set<?>) tags.getClass().getMethod("getBaseSpirits").invoke(tags);
                assertEquals(1, spirits.size());
                assertEquals("RUM", spirits.iterator().next().toString());
            }
        } finally {
            Files.deleteIfExists(jar);
        }
    }
}
