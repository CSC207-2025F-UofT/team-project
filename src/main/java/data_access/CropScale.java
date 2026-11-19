package data_access;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Picks the scale factor m for a formula picture with the tree model trained by
 * {@code src/neutral network/crop.py} (LightGBM, exported as {@code crop_scale.json}). The scale then
 * makes the tallest band {@code 112 / (tallest band * m)} instead of {@code 112 / tallest band}: m &lt; 1
 * makes the characters bigger, m &gt; 1 smaller, m = 1 is the plain {@link FormulaLayout} rule.
 *
 * <p>The features are the same as {@code crop_scale.ink_features} in Python, item for item, and
 * training uses the same file, so the model sees pictures scaled the same way at training and at run
 * time. Change both sides together; {@code CropScaleTest} checks them against Python's numbers.
 */
final class CropScale {
    static final String FORMAT = "crop-scale-trees-v1";
    static final Path DEFAULT_PATH = Path.of("src", "main", "java", "model", "checkpoints", "crop_scale.json");
    /** Always m = 1, the plain rule. */
    static final CropScale RULE = new CropScale();

    private static final double[] GAP_FRACTIONS = {0.005, 0.01, 0.02, 0.03, 0.05, 0.08};
    private static final String[] GAP_NAMES = {"0.005", "0.01", "0.02", "0.03", "0.05", "0.08"};
    private static final int[] HEIGHT_QUANTILES = {10, 25, 50, 75, 90, 100};
    private static final double SHEAR_UNIT = 0.05;
    private static final int SHEAR_STEPS = 4;              // shear = k * SHEAR_UNIT for k in -4..4
    private static final int MAX_SHEAR_PIXELS = 200_000;
    private static CropScale defaultModel;

    private final String method;
    private final double[] candidates;
    private final double[] log2Candidates;
    private final int ruleIndex;
    private final String[] featureNames;
    private final Tree[] trees;

    private record Tree(int[] split, double[] threshold, int[] left, int[] right, double[] leaf) {
        double predict(double[] row) {
            if (split.length == 0) {
                return leaf[0];
            }
            int node = 0;
            while (node >= 0) {
                node = row[split[node]] <= threshold[node] ? left[node] : right[node];
            }
            return leaf[~node];
        }
    }

    private CropScale() {
        method = "rule";
        candidates = new double[]{1.0};
        log2Candidates = new double[]{0.0};
        ruleIndex = 0;
        featureNames = new String[0];
        trees = new Tree[0];
    }

    private CropScale(JsonObject json) {
        if (!FORMAT.equals(json.get("format").getAsString())) {
            throw new IllegalArgumentException("Unknown scale model format: " + json.get("format"));
        }
        method = json.get("method").getAsString();
        candidates = doubles(json.getAsJsonArray("candidates"));
        log2Candidates = doubles(json.getAsJsonArray("log2_candidates"));
        ruleIndex = json.get("rule_index").getAsInt();
        JsonArray names = json.getAsJsonArray("feature_names");
        featureNames = new String[names.size()];
        for (int i = 0; i < featureNames.length; i++) {
            featureNames[i] = names.get(i).getAsString();
        }
        JsonArray list = json.getAsJsonArray("trees");
        trees = new Tree[list.size()];
        for (int i = 0; i < trees.length; i++) {
            JsonObject t = list.get(i).getAsJsonObject();
            trees[i] = new Tree(ints(t.getAsJsonArray("split")), doubles(t.getAsJsonArray("threshold")),
                    ints(t.getAsJsonArray("left")), ints(t.getAsJsonArray("right")), doubles(t.getAsJsonArray("leaf")));
        }
        if (!method.equals("direct") && !method.equals("surface")) {
            throw new IllegalArgumentException("Unknown scale model method: " + method);
        }
    }

    static CropScale load(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return new CropScale(JsonParser.parseReader(reader).getAsJsonObject());
        }
    }

    /** The model at {@link #DEFAULT_PATH}, read once. Training used it, so a missing file is an error. */
    static synchronized CropScale defaultModel() {
        if (defaultModel == null) {
            try {
                defaultModel = load(DEFAULT_PATH);
            } catch (IOException e) {
                throw new UncheckedIOException("Cannot read the scale model " + DEFAULT_PATH
                        + "; copy checkpoints/crop/crop_scale.json there (python crop.py fit writes it)", e);
            }
        }
        return defaultModel;
    }

    /** @param mask ink cropped to its bounding box, row by row, {@code width * height} long */
    double bandScale(boolean[] mask, int width, int height) {
        return trees.length == 0 ? 1.0 : candidates[chooseIndex(features(mask, width, height))];
    }

    int chooseIndex(Map<String, Double> features) {
        double[] row = new double[featureNames.length + 1];
        for (int i = 0; i < featureNames.length; i++) {
            Double value = features.get(featureNames[i]);
            if (value == null) {
                throw new IllegalArgumentException("The scale model needs a feature Java does not compute: "
                        + featureNames[i]);
            }
            row[i] = value;
        }
        if (method.equals("surface")) {
            // predicted change of the loss against the plain rule for each m; keep the rule unless one is lower
            int best = ruleIndex;
            double bestDelta = 0.0;
            for (int k = 0; k < candidates.length; k++) {
                row[featureNames.length] = log2Candidates[k];
                double delta = predict(row);
                if (k != ruleIndex && delta < bestDelta) {
                    best = k;
                    bestDelta = delta;
                }
            }
            return best;
        }
        double predicted = predict(row);
        int best = 0;
        for (int k = 1; k < candidates.length; k++) {
            if (Math.abs(predicted - log2Candidates[k]) < Math.abs(predicted - log2Candidates[best])) {
                best = k;
            }
        }
        return best;
    }

    private double predict(double[] row) {
        double total = 0.0;
        for (Tree tree : trees) {
            total += tree.predict(row);
        }
        return total;
    }

    // ---------- features: crop_scale.ink_features ----------

    static LinkedHashMap<String, Double> features(boolean[] mask, int w, int h) {
        LinkedHashMap<String, Double> f = new LinkedHashMap<>();
        boolean[] rowHasInk = new boolean[h];
        long[] profile = new long[h];
        int inkCount = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (mask[y * w + x]) {
                    profile[y]++;
                    rowHasInk[y] = true;
                    inkCount++;
                }
            }
        }
        int ink = Math.max(inkCount, 1);
        List<int[]> spans = FormulaLayout.rowSpans(rowHasInk);
        double rule = Math.max(FormulaLayout.tallestBand(spans, h, FormulaLayout.GAP_MIN_PX),
                FormulaLayout.FLAT_FRACTION * w);
        f.put("log_aspect", Math.log((double) w / h));
        f.put("density", ink / (double) ((long) h * w));
        f.put("rule_band", rule / h);

        for (int i = 0; i < GAP_FRACTIONS.length; i++) {
            List<Double> bands = new ArrayList<>(
                    FormulaLayout.bandHeights(spans, h, FormulaLayout.GAP_MIN_PX, GAP_FRACTIONS[i]));
            if (bands.isEmpty()) {
                bands.add(0.0);
            }
            bands.sort(Collections.reverseOrder());
            String g = GAP_NAMES[i];
            f.put("bands@" + g, (double) bands.size());
            f.put("band1@" + g, bands.get(0) / h);
            f.put("band2@" + g, (bands.size() > 1 ? bands.get(1) : 0.0) / h);
            f.put("band_med@" + g, percentile(toArray(bands), 50) / h);
        }

        double[] smooth = movingAverage(profile, Math.max(1, h / 50));
        double top = Arrays.stream(smooth).max().orElse(0);
        f.put("profile_peaks", (double) countPeaks(smooth, 0.2 * top));
        int empty = 0;
        for (double value : smooth) {
            if (value < 0.05 * top) {
                empty++;
            }
        }
        f.put("empty_rows", (double) empty / h);
        double[] ac = autocorrelationPeak(profile);
        f.put("ac_lag", ac[0]);
        f.put("ac_peak", ac[1]);

        List<int[]> boxes = new ArrayList<>();                  // {top, left, bottom, right, area}
        for (int[] box : components(mask, w, h)) {
            if (box[4] >= Math.max(2, 0.002 * ink)) {
                boxes.add(box);
            }
        }
        double[] hs = new double[boxes.size()];
        double[] ws = new double[boxes.size()];
        double widest = 0;
        for (int i = 0; i < boxes.size(); i++) {
            hs[i] = boxes.get(i)[2] - boxes.get(i)[0];
            ws[i] = boxes.get(i)[3] - boxes.get(i)[1];
            widest = Math.max(widest, ws[i]);
        }
        f.put("cc_count", Math.log1p(boxes.size()));
        for (int q : HEIGHT_QUANTILES) {
            f.put("cc_h_q" + q, percentile(hs, q) / h);
        }
        f.put("cc_h_med_vs_rule", percentile(hs, 50) / rule);
        f.put("cc_w_med", percentile(ws, 50) / w);
        f.put("cc_w_max", widest / w);
        List<int[]> lines = new ArrayList<>();
        for (int i = 0; i < boxes.size(); i++) {
            if (ws[i] > 3 * hs[i] && ws[i] > 0.15 * w) {
                lines.add(boxes.get(i));
            }
        }
        f.put("thin_lines", (double) lines.size());
        int bars = 0;                                           // ink above and below a line: a fraction bar
        for (int[] line : lines) {
            int middle2 = line[0] + line[2];
            boolean above = false;
            boolean below = false;
            for (int[] b : boxes) {
                if (b == line || b[3] <= line[1] || b[1] >= line[3]) {
                    continue;
                }
                above |= b[0] + b[2] < middle2;
                below |= b[0] + b[2] > middle2;
            }
            if (above && below) {
                bars++;
            }
        }
        f.put("fraction_bars", (double) bars);

        List<Double> runs = new ArrayList<>();
        for (int y = 0; y < h; y++) {
            int run = 0;
            for (int x = 0; x <= w; x++) {
                if (x < w && mask[y * w + x]) {
                    run++;
                } else if (run > 0) {
                    runs.add((double) run);
                    run = 0;
                }
            }
        }
        f.put("stroke", percentile(toArray(runs), 50) / h);

        shearFeatures(mask, w, h, inkCount, f);
        return f;
    }

    /** Linear interpolation between the sorted values at (n-1)*q/100; 0 when empty. */
    static double percentile(double[] values, int q) {
        if (values.length == 0) {
            return 0.0;
        }
        double[] a = values.clone();
        Arrays.sort(a);
        double position = (a.length - 1) * q / 100.0;
        int lo = (int) Math.floor(position);
        int hi = Math.min(lo + 1, a.length - 1);
        return a[lo] + (a[hi] - a[lo]) * (position - lo);
    }

    /** Mean of [i - k/2, i - k/2 + k), counting outside rows as 0. */
    static double[] movingAverage(long[] values, int k) {
        int n = values.length;
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + values[i];
        }
        double[] out = new double[n];
        for (int i = 0; i < n; i++) {
            int lo = Math.min(Math.max(i - k / 2, 0), n);
            int hi = Math.min(Math.max(i - k / 2 + k, 0), n);
            out[i] = (double) (prefix[hi] - prefix[lo]) / k;
        }
        return out;
    }

    /**
     * Peaks of the profile padded with 0 at both ends: v[i] &gt; v[i-1] and v[i] &gt;= v[i+1]. A peak counts
     * when its height above the higher of its two side minima (each side walked until the first higher
     * point or the end) is at least {@code minProminence}.
     */
    static int countPeaks(double[] values, double minProminence) {
        double[] v = new double[values.length + 2];
        System.arraycopy(values, 0, v, 1, values.length);
        int count = 0;
        for (int i = 1; i < v.length - 1; i++) {
            if (!(v[i] > v[i - 1] && v[i] >= v[i + 1])) {
                continue;
            }
            double leftMin = Double.POSITIVE_INFINITY;
            for (int j = i - 1; j >= 0 && !(v[j] > v[i]); j--) {
                leftMin = Math.min(leftMin, v[j]);
            }
            double rightMin = Double.POSITIVE_INFINITY;
            for (int j = i + 1; j < v.length && !(v[j] > v[i]); j++) {
                rightMin = Math.min(rightMin, v[j]);
            }
            if (v[i] - Math.max(leftMin, rightMin) >= minProminence) {
                count++;
            }
        }
        return count;
    }

    /** Highest autocorrelation of the centred profile for lags in [0.1n, 0.9n) -> {lag / n, value / lag-0 value}. */
    static double[] autocorrelationPeak(long[] p) {
        int n = p.length;
        int lo = Math.max(1, (int) (0.1 * n));
        int hi = (int) (0.9 * n);
        if (hi <= lo) {
            return new double[]{0.0, 0.0};
        }
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + p[i];
        }
        double mu = (double) prefix[n] / n;
        double zero = autocorrelation(p, prefix, mu, 0);
        if (zero <= 0) {
            return new double[]{0.0, 0.0};
        }
        int bestLag = lo;
        double best = autocorrelation(p, prefix, mu, lo);
        for (int lag = lo + 1; lag < hi; lag++) {
            double value = autocorrelation(p, prefix, mu, lag);
            if (value > best) {
                bestLag = lag;
                best = value;
            }
        }
        return new double[]{(double) bestLag / n, best / zero};
    }

    private static double autocorrelation(long[] p, long[] prefix, double mu, int lag) {
        int n = p.length;
        long products = 0;
        for (int i = 0; i + lag < n; i++) {
            products += p[i] * p[i + lag];
        }
        long headsTails = prefix[n - lag] + (prefix[n] - prefix[lag]);
        return (double) products - mu * (double) headsTails + (double) (n - lag) * mu * mu;
    }

    /** 8-connected components -> {top, left, bottom, right, area}, bottom and right not included. */
    static List<int[]> components(boolean[] mask, int w, int h) {
        boolean[] seen = new boolean[mask.length];
        List<int[]> found = new ArrayList<>();
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int start = 0; start < mask.length; start++) {
            if (!mask[start] || seen[start]) {
                continue;
            }
            seen[start] = true;
            queue.add(start);
            int top = h, left = w, bottom = -1, right = -1, area = 0;
            while (!queue.isEmpty()) {
                int index = queue.poll();
                int y = index / w;
                int x = index % w;
                area++;
                top = Math.min(top, y);
                bottom = Math.max(bottom, y);
                left = Math.min(left, x);
                right = Math.max(right, x);
                for (int ny = Math.max(0, y - 1); ny <= Math.min(h - 1, y + 1); ny++) {
                    for (int nx = Math.max(0, x - 1); nx <= Math.min(w - 1, x + 1); nx++) {
                        int next = ny * w + nx;
                        if (mask[next] && !seen[next]) {
                            seen[next] = true;
                            queue.add(next);
                        }
                    }
                }
            }
            found.add(new int[]{top, left, bottom + 1, right + 1, area});
        }
        return found;
    }

    /** Which shear makes the row profile sharpest (largest sum of squares), and the bands after it. */
    private static void shearFeatures(boolean[] mask, int w, int h, int inkCount, Map<String, Double> f) {
        int step = inkCount > MAX_SHEAR_PIXELS ? (inkCount + MAX_SHEAR_PIXELS - 1) / MAX_SHEAR_PIXELS : 1;
        int n = (inkCount + step - 1) / step;
        int[] ys = new int[n];
        double[] offset = new double[n];
        int seen = 0;
        int kept = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (mask[y * w + x]) {
                    if (seen % step == 0) {
                        ys[kept] = y;
                        offset[kept] = x - w / 2.0;
                        kept++;
                    }
                    seen++;
                }
            }
        }
        double bestSharp = -1.0;
        double sharp0 = 1.0;
        int bestStep = 0;
        long[] bestProfile = null;
        long[] shifted = new long[n];
        for (int k = -SHEAR_STEPS; k <= SHEAR_STEPS; k++) {
            double s = k * SHEAR_UNIT;
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                shifted[i] = (long) Math.floor(ys[i] + s * offset[i] + 0.5);
                min = Math.min(min, shifted[i]);
                max = Math.max(max, shifted[i]);
            }
            long[] profile = new long[(int) (max - min + 1)];
            for (int i = 0; i < n; i++) {
                profile[(int) (shifted[i] - min)]++;
            }
            long squares = 0;
            for (long count : profile) {
                squares += count * count;
            }
            double sharp = (double) squares / (double) ((long) n * n);
            if (k == 0) {
                sharp0 = sharp;
            }
            if (sharp > bestSharp) {
                bestSharp = sharp;
                bestStep = k;
                bestProfile = profile;
            }
        }
        boolean[] occupied = new boolean[bestProfile.length];
        for (int i = 0; i < occupied.length; i++) {
            occupied[i] = bestProfile[i] > 0;
        }
        List<Double> sheared = FormulaLayout.bandHeights(FormulaLayout.rowSpans(occupied), bestProfile.length,
                FormulaLayout.GAP_MIN_PX, FormulaLayout.GAP_FRACTION);
        f.put("shear_best", bestStep * SHEAR_UNIT);
        f.put("shear_gain", bestSharp / sharp0);
        f.put("shear_bands", (double) sheared.size());
        f.put("shear_band1", Collections.max(sheared) / h);
    }

    private static double[] toArray(List<Double> values) {
        double[] out = new double[values.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = values.get(i);
        }
        return out;
    }

    private static double[] doubles(JsonArray array) {
        double[] out = new double[array.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = array.get(i).getAsDouble();
        }
        return out;
    }

    private static int[] ints(JsonArray array) {
        int[] out = new int[array.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = array.get(i).getAsInt();
        }
        return out;
    }
}
