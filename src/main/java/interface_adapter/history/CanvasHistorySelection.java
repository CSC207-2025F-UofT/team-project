package interface_adapter.history;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

/** Immutable metadata snapshot for copying already-placed History assets between UI surfaces.
 * The original raster is retained for graphs; LaTeX/result are retained for calculations.
 * It does not own or mutate the Canvas's live selection.
 */
public record CanvasHistorySelection(List<Asset> assets) {
    public CanvasHistorySelection {
        assets = List.copyOf(Objects.requireNonNull(assets, "assets"));
        if (assets.isEmpty()) throw new IllegalArgumentException("No History assets selected");
    }

    public record Asset(BufferedImage image, String latex, String result,
                        double x, double y, double width, double height) {
        public Asset {
            Objects.requireNonNull(image, "image");
            latex = latex == null ? "" : latex;
            result = result == null ? "" : result;
            if (width <= 0 || height <= 0) throw new IllegalArgumentException("Invalid History asset bounds");
        }
        public boolean isGraph() { return latex.isBlank(); }
    }
}
