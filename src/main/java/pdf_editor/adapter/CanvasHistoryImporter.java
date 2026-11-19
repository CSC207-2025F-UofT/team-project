package pdf_editor.adapter;

import interface_adapter.history.CanvasHistorySelection;
import pdf_editor.MathLibrary;
import java.util.Objects;

/** Adapter from a Canvas History selection into structured PDF materials.
 * Source Canvas entries remain untouched; each imported entry appears in Library and on PDF.
 */
public final class CanvasHistoryImporter {
    private CanvasHistoryImporter() { }

    public static int importAt(CanvasHistorySelection selection, PdfEditorController controller,
                               float pageX, float pageY) {
        Objects.requireNonNull(selection, "selection");
        Objects.requireNonNull(controller, "controller");
        double minX = selection.assets().stream().mapToDouble(CanvasHistorySelection.Asset::x).min().orElse(0);
        double minY = selection.assets().stream().mapToDouble(CanvasHistorySelection.Asset::y).min().orElse(0);
        for (CanvasHistorySelection.Asset asset : selection.assets()) {
            MathLibrary.Material material;
            if (asset.isGraph()) {
                material = controller.library().addGraph("", asset.image());
            } else if (asset.result().isBlank()) {
                material = controller.library().addFormula(asset.latex());
            } else {
                material = controller.library().addCalculation(asset.latex(), asset.result());
            }
            float width = (float) Math.max(85, Math.min(440, asset.width()));
            float height = asset.isGraph()
                    ? width * asset.image().getHeight() / Math.max(1f, asset.image().getWidth())
                    : (float) Math.max(50, Math.min(160, asset.height()));
            if (height > 575) { width *= 575 / height; height = 575; }
            float x = pageX + (float) (asset.x() - minX);
            float y = pageY + (float) (asset.y() - minY);
            controller.place(material, x, y, width, height);
        }
        return selection.assets().size();
    }
}
