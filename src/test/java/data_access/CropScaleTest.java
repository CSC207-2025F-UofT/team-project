package data_access;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks the Java scale model against Python: {@code crop_scale_fixtures.json} holds ink masks with the
 * features and the choice {@code crop_scale.py} computed for them
 * ({@code python tests/generate_crop_scale_fixtures.py} writes it).
 */
class CropScaleTest {
    private static final Path FIXTURES = Path.of("src", "test", "java", "data_access", "crop_scale_fixtures.json");

    private static JsonObject fixtures() throws IOException {
        try (Reader reader = Files.newBufferedReader(FIXTURES, StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    private static boolean[] decode(JsonArray runs, int size) {
        boolean[] mask = new boolean[size];
        int position = 0;
        boolean value = false;
        for (JsonElement run : runs) {
            int length = run.getAsInt();
            for (int i = 0; i < length; i++) {
                mask[position++] = value;
            }
            value = !value;
        }
        assertEquals(size, position, "run lengths add up to the image size");
        return mask;
    }

    @Test
    void featuresMatchPythonOnEveryFixture() throws IOException {
        for (JsonElement element : fixtures().getAsJsonArray("cases")) {
            JsonObject c = element.getAsJsonObject();
            int w = c.get("width").getAsInt();
            int h = c.get("height").getAsInt();
            Map<String, Double> java = CropScale.features(decode(c.getAsJsonArray("runs"), w * h), w, h);
            JsonObject python = c.getAsJsonObject("features");
            assertEquals(python.keySet(), java.keySet(), c.get("name").getAsString());
            for (String name : python.keySet()) {
                double expected = python.get(name).getAsDouble();
                // Integer counts and fixed-order arithmetic are exact; only log/log1p may differ in the last bit.
                assertEquals(expected, java.get(name), 1e-12 * Math.max(1, Math.abs(expected)),
                        c.get("name").getAsString() + " " + name);
            }
        }
    }

    @Test
    void deployedModelPicksTheSameScaleAsPython() throws IOException {
        JsonObject all = fixtures();
        CropScale model = CropScale.defaultModel();
        for (JsonElement element : all.getAsJsonArray("cases")) {
            JsonObject c = element.getAsJsonObject();
            int w = c.get("width").getAsInt();
            int h = c.get("height").getAsInt();
            LinkedHashMap<String, Double> features = CropScale.features(decode(c.getAsJsonArray("runs"), w * h), w, h);
            assertEquals(c.get("choice").getAsInt(), model.chooseIndex(features), c.get("name").getAsString());
        }
    }

    @Test
    void ruleKeepsTheScaleAtOne() {
        boolean[] mask = new boolean[30 * 10];
        java.util.Arrays.fill(mask, true);
        assertEquals(1.0, CropScale.RULE.bandScale(mask, 30, 10));
    }

    @Test
    void scaledPictureStillFitsTheModelInput() {
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(300, 80,
                java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g = image.createGraphics();
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, 300, 80);
        g.setColor(java.awt.Color.BLACK);
        g.setFont(g.getFont().deriveFont(40f));
        g.drawString("x+1=2", 20, 60);
        g.dispose();
        java.awt.image.BufferedImage out = FormulaImagePreprocessor.prepare(image);
        assertTrue(out.getHeight() >= 128 && out.getHeight() % 16 == 0 && out.getWidth() % 16 == 0);
    }

    @Test
    void helpersFollowTheirPythonDefinitions() {
        assertEquals(2.5, CropScale.percentile(new double[]{4, 1, 3, 2}, 50), 0);
        assertEquals(0.0, CropScale.percentile(new double[0], 50), 0);
        assertEquals(1, CropScale.countPeaks(new double[]{0, 5, 5, 0}, 1));
        assertEquals(2, CropScale.countPeaks(new double[]{1, 5, 1, 4, 1}, 2));
        assertEquals(1, CropScale.countPeaks(new double[]{1, 5, 4, 4.5, 1}, 2));   // the small bump is not prominent
        double[] mean = CropScale.movingAverage(new long[]{3, 6, 9}, 2);
        assertEquals(1.5, mean[0], 0);
        assertEquals(4.5, mean[1], 0);
        assertEquals(7.5, mean[2], 0);
        // diagonal neighbours join (8-connected); a gap of one pixel separates
        assertEquals(1, CropScale.components(new boolean[]{true, false, false, false, true, false, false, false, true},
                3, 3).size());
        assertEquals(2, CropScale.components(new boolean[]{true, false, true}, 3, 1).size());
    }
}
