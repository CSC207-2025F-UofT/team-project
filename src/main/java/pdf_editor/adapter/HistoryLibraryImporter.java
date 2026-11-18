package pdf_editor.adapter;

import interface_adapter.history.HistoryItem;
import pdf_editor.MathLibrary;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.Objects;

/** History -> PDF Library adapter. Preserve formula/result semantics and original graph pixels. */
public final class HistoryLibraryImporter {
    private HistoryLibraryImporter() { }

    public static MathLibrary.Material importRecord(HistoryItem record, MathLibrary library) {
        Objects.requireNonNull(record, "record");
        Objects.requireNonNull(library, "library");
        if (!record.isGraph()) {
            return record.getAnswer().isBlank()
                    ? library.addFormula(record.getLatex())
                    : library.addCalculation(record.getLatex(), record.getAnswer());
        }
        try {
            BufferedImage graph = ImageIO.read(new ByteArrayInputStream(record.imagePng()));
            if (graph == null) throw new IOException("Unsupported graph PNG");
            return library.addGraph(record.getLatex(), graph);
        } catch (IOException invalid) {
            throw new IllegalArgumentException("History graph has invalid PNG content", invalid);
        }
    }
}
