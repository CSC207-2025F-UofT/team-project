package pdf_editor.adapter;

import history.domain.HistoryEntry;
import history.use_case.HistoryInteractor;
import history.use_case.HistoryRepository;
import history.data_access.FileHistoryRepository;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import pdf_editor.domain.PdfDocument;
import pdf_editor.use_case.AutoLayoutInteractor;
import pdf_editor.use_case.DocumentEditor;
import pdf_editor.use_case.PdfExportInteractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class FormulaRevisionCoordinatorTest {
    private static final class Repository implements HistoryRepository {
        final List<HistoryEntry> records = new ArrayList<>();
        @Override public boolean saveIfAbsent(HistoryEntry entry) {
            if (records.stream().anyMatch(e -> e.contentKey().equals(entry.contentKey()))) return false;
            records.add(entry);
            return true;
        }
        @Override public List<HistoryEntry> findAll() { return List.copyOf(records); }
        @Override public boolean deleteById(String id) { return records.removeIf(e -> e.id().equals(id)); }
    }
    private static PdfEditorController editor(MathLibrary library) {
        DocumentEditor edit = new DocumentEditor();
        return new PdfEditorController(library, edit, new AutoLayoutInteractor(edit),
                new PdfExportInteractor((d, path) -> { }, (d, scale) -> List.of()));
    }
    @Test void pdfRevisionRecalculatesAndUpdatesLibraryHistoryAndCopies() throws IOException {
        MathLibrary library = new MathLibrary();
        Repository storage = new Repository();
        HistoryInteractor history = new HistoryInteractor(storage);
        history.recordCalculation("2+3", "5");
        MathLibrary.Material original = library.addCalculation("2+3", "5");
        PdfEditorController pdf = editor(library);
        PdfDocument.Placement first = pdf.place(original, 30, 30, 150, 70);
        PdfDocument.Placement second = pdf.place(original, 200, 30, 150, 70);
        pdf.select(first.id());
        AtomicInteger notices = new AtomicInteger();
        library.onChange(notices::incrementAndGet);
        FormulaRevisionCoordinator sync = new FormulaRevisionCoordinator(library, history,
                latex -> latex.equals("4+5") ? "9" : "");
        String result = sync.recalculate("4+5");
        assertEquals("9", result);
        sync.editPdf(pdf, original, "4+5", result);
        assertEquals("4+5", library.find(original.id()).formula());
        assertEquals("9", library.find(original.id()).result());
        assertEquals("4+5", pdf.placements().get(0).material().formula());
        assertEquals("9", pdf.placements().get(1).material().result());
        assertEquals(second.id(), pdf.placements().get(1).id());
        assertEquals(1, notices.get());
        assertEquals(1, history.list().size());
        assertEquals("4+5", history.list().get(0).formula());
        PdfDocument newDocument = pdf.documentSnapshot();
        assertTrue(pdf.undo());
        sync.reconcileDocumentUndo(newDocument, pdf.documentSnapshot());
        assertEquals("2+3", pdf.placements().get(0).material().formula());
        assertEquals("2+3", library.find(original.id()).formula());
        assertEquals("2+3", history.list().get(0).formula());
        PdfDocument undone = pdf.documentSnapshot();
        assertTrue(pdf.redo());
        sync.reconcileDocumentUndo(undone, pdf.documentSnapshot());
        assertEquals("4+5", pdf.placements().get(0).material().formula());
        assertEquals("4+5", library.find(original.id()).formula());
        assertEquals("4+5", history.list().get(0).formula());
    }
    @Test void editAHistoryImportedPdfMaterialAddsCorrespondingLibraryMaterial() throws IOException {
        MathLibrary library = new MathLibrary();
        HistoryInteractor history = new HistoryInteractor(new Repository());
        history.recordCalculation("3+3", "6");
        PdfEditorController pdf = editor(library);
        // Import without sharing a Library ID, as Add History to PDF does.
        var imported = new pdf_editor.domain.PdfMaterial("Imported", "3+3", "6", null, 0, 0) {};
        var placed = pdf.place(imported, 10, 20, 140, 70);
        pdf.select(placed.id());
        FormulaRevisionCoordinator sync = new FormulaRevisionCoordinator(library, history, s -> "7");
        sync.editPdf(pdf, imported, "3+4", "7");
        assertEquals(1, library.items().size());
        assertEquals("3+4", library.items().get(0).formula());
        assertEquals("7", history.list().get(0).result());
    }
    @Test void libraryRevisionUpdatesPdfAndHistoryAndPreservesMaterialId() throws IOException {
        MathLibrary library = new MathLibrary();
        HistoryInteractor history = new HistoryInteractor(new Repository());
        history.recordFormula("x+2");
        MathLibrary.Material material = library.addFormula("x+2");
        PdfEditorController pdf = editor(library);
        var placed = pdf.place(material, 10, 10, 200, 80);
        var sync = new FormulaRevisionCoordinator(library, history, s -> "2x");
        sync.editLibrary(pdf, material, "x+x", "2x");
        assertNotNull(library.find(material.id()));
        assertEquals("x+x", library.find(material.id()).formula());
        assertEquals("x+x", pdf.placements().get(0).material().formula());
        assertEquals(placed.id(), pdf.placements().get(0).id());
        assertEquals("2x", history.list().get(0).result());
    }
    @TempDir Path directory;
    @Test void revisedCalculationIsPersistedAcrossHistoryReload() throws IOException {
        HistoryInteractor first = new HistoryInteractor(new FileHistoryRepository(directory));
        first.recordCalculation("2+2", "4");
        MathLibrary library = new MathLibrary();
        MathLibrary.Material material = library.addCalculation("2+2", "4");
        PdfEditorController pdf = editor(library);
        var placement = pdf.place(material, 5, 10, 180, 60);
        pdf.select(placement.id());
        new FormulaRevisionCoordinator(library, first, s -> "5")
                .editPdf(pdf, material, "2+3", "5");
        HistoryInteractor reopened = new HistoryInteractor(new FileHistoryRepository(directory));
        assertEquals(1, reopened.list().size());
        assertEquals("2+3", reopened.list().get(0).formula());
        assertEquals("5", reopened.list().get(0).result());
    }

    @Test void persistenceFailureDoesNotModifyPdfOrLibrary() throws IOException {
        HistoryRepository failing = new HistoryRepository() {
            @Override public boolean saveIfAbsent(HistoryEntry entry) throws IOException {
                throw new IOException("disk full");
            }
            @Override public List<HistoryEntry> findAll() { return List.of(); }
            @Override public boolean deleteById(String id) { return false; }
        };
        MathLibrary library = new MathLibrary();
        var material = library.addCalculation("2+2", "4");
        PdfEditorController pdf = editor(library);
        var placement = pdf.place(material, 0, 0, 160, 80);
        pdf.select(placement.id());
        try {
            new FormulaRevisionCoordinator(library, new HistoryInteractor(failing), s -> "5")
                    .editPdf(pdf, material, "2+3", "5");
            throw new AssertionError("Should not overwrite PDF if History cannot save");
        } catch (IOException expected) {
            assertEquals("2+2", pdf.placements().get(0).material().formula());
            assertEquals("2+2", library.find(material.id()).formula());
        }
    }

    @Test void editingCanvasHistoryWithoutEarlierLibraryCopyStillSynchronizesBoth() throws IOException {
        MathLibrary library = new MathLibrary();
        HistoryInteractor history = new HistoryInteractor(new Repository());
        history.recordCalculation("6+1", "7");
        PdfEditorController pdf = editor(library);
        FormulaRevisionCoordinator sync = new FormulaRevisionCoordinator(library, history, s -> "9");
        sync.editCanvasHistory(pdf, "6+1", "7", "6+3", "9");
        assertEquals(1, library.items().size());
        assertEquals("6+3", library.items().get(0).formula());
        assertEquals("9", history.list().get(0).result());
    }

    @Test void unrelatedMaterialMustNotChange() throws IOException {
        MathLibrary library = new MathLibrary();
        var unrelated = library.addCalculation("a", "b");
        var edited = library.addCalculation("2+2", "4");
        PdfEditorController pdf = editor(library);
        var selected = pdf.place(edited, 0, 0, 120, 70);
        pdf.select(selected.id());
        new FormulaRevisionCoordinator(library, null, s -> "5")
                .editPdf(pdf, edited, "2+3", "5");
        assertEquals("a", library.find(unrelated.id()).formula());
        assertEquals("b", library.find(unrelated.id()).result());
    }
    @Test void libraryObserversSeeAlreadyPersistedHistory() throws IOException {
        MathLibrary library = new MathLibrary();
        HistoryInteractor history = new HistoryInteractor(new Repository());
        history.recordCalculation("1+1", "2");
        var material = library.addCalculation("1+1", "2");
        PdfEditorController pdf = editor(library);
        var placement = pdf.place(material, 10, 20, 150, 70);
        pdf.select(placement.id());
        library.onChange(() -> {
            try { assertEquals("3", history.list().get(0).result()); }
            catch (IOException exception) { throw new AssertionError(exception); }
        });
        new FormulaRevisionCoordinator(library, history, latex -> "3")
                .editPdf(pdf, material, "1+2", "3");
    }
    @Test void manualEntryPreservesUnsupportedExpressionInPdfLibraryAndHistoryWithoutCalculation()
            throws IOException {
        MathLibrary library = new MathLibrary();
        HistoryInteractor history = new HistoryInteractor(new Repository());
        history.recordCalculation("2+2", "4");
        MathLibrary.Material source = library.addCalculation("2+2", "4");
        PdfEditorController pdf = editor(library);
        PdfDocument.Placement placement = pdf.place(source, 15, 20, 150, 60);
        pdf.select(placement.id());
        FormulaRevisionCoordinator coordinator = new FormulaRevisionCoordinator(library, history,
                latex -> { throw new AssertionError("Manual entries must bypass US2"); });
        var entered = pdf_editor.use_case.FormulaEditSubmission.submit("1+1=3",
                pdf_editor.use_case.FormulaEditSubmission.Mode.MANUAL, null);
        coordinator.editPdf(pdf, source, entered.formula(), entered.result());
        assertEquals("1+1=3", pdf.placements().get(0).material().formula());
        assertEquals("", pdf.placements().get(0).material().result());
        assertEquals("1+1=3", library.find(source.id()).formula());
        assertEquals("", library.find(source.id()).result());
        assertEquals(1, history.list().size());
        assertEquals("1+1=3", history.list().get(0).formula());
        assertEquals("", history.list().get(0).result());
        PdfDocument saved = pdf.documentSnapshot();
        assertTrue(pdf.undo());
        coordinator.reconcileDocumentUndo(saved, pdf.documentSnapshot());
        assertEquals("2+2", library.find(source.id()).formula());
        assertEquals("4", history.list().get(0).result());
    }

    @Test void manualEntryFromLibraryRefreshesPdfCopiesAndHistory() throws IOException {
        MathLibrary library = new MathLibrary();
        HistoryInteractor history = new HistoryInteractor(new Repository());
        history.recordFormula("x+1");
        MathLibrary.Material source = library.addFormula("x+1");
        PdfEditorController pdf = editor(library);
        pdf.place(source, 10, 15, 170, 70);
        pdf.place(source, 200, 15, 170, 70);
        FormulaRevisionCoordinator coordinator = new FormulaRevisionCoordinator(library, history,
                latex -> {throw new AssertionError("Manual entry must not evaluate");});
        coordinator.editLibrary(pdf, source, "i^2=-1", "");
        assertEquals("i^2=-1", library.find(source.id()).formula());
        assertEquals("i^2=-1", pdf.placements().get(0).material().formula());
        assertEquals("i^2=-1", pdf.placements().get(1).material().formula());
        assertEquals("i^2=-1", history.list().get(0).formula());
    }

}
