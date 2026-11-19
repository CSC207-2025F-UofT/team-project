package pdf_editor.view;

import pdf_editor.adapter.PdfEditorController;
import pdf_editor.adapter.PdfBodyFlowLayout;
import pdf_editor.adapter.PdfWrapTextLayout;
import pdf_editor.adapter.CanvasHistoryImporter;
import interface_adapter.history.CanvasHistorySelection;
import pdf_editor.adapter.PageZoomModel;
import pdf_editor.adapter.PageCoordinateMapper;
import pdf_editor.data_access.MaterialRenderer;
import pdf_editor.data_access.NoteRenderer;
import pdf_editor.domain.PdfDocument;
import pdf_editor.MathLibrary;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;

/** Swing page interaction adapter: hit-testing and rendering; all edits go through controller. */
public final class PdfPageCanvas extends JPanel {
    public interface Listener {
        void contextMenu(MouseEvent event);
        void editNote();
        void editFormula();
        default void typeInDocument() { }
        default void typeInDocumentAt(float x, float y) { typeInDocument(); }
        default void bodyOverflow() { }
        default void bodyChanged() { }
    }
    private static final int MARGIN = 28;
    private final PdfEditorController controller;
    private final Listener listener;
    private final PageZoomModel zoomModel = new PageZoomModel();
    public double zoom() { return zoomModel.zoom(); }
    public double setZoom(double zoom) {
        finishInlineEdit();
        zoomModel.set(zoom);
        updateSize();
        if (inlineNote != null) {
            PdfDocument.Placement note = controller.selected();
            if (note != null) positionInlineEditor(note);
        }
        revalidate(); repaint();
        return zoomModel.zoom();
    }
    public double adjustZoom(double rotation) { return setZoom(zoomModel.adjustWheel(rotation)); }
    public double fitZoom(int viewportWidth, int viewportHeight) {
        return setZoom(zoomModel.fit(viewportWidth - MARGIN * 2, viewportHeight - MARGIN * 2,
                (int) PdfDocument.PAGE_WIDTH, (int) PdfDocument.PAGE_HEIGHT));
    }
    private void updateSize() {
        double z = zoom();
        setPreferredSize(new Dimension((int)Math.ceil(PdfDocument.PAGE_WIDTH * z) + MARGIN * 2,
                (int)Math.ceil(PdfDocument.PAGE_HEIGHT * z) + MARGIN * 2));
    }
    public float pageX(int screenX) { return toPageX(screenX); }
    public float pageY(int screenY) { return toPageY(screenY); }
    private float toPageX(int screenX) { return PageCoordinateMapper.screenToPage(screenX, MARGIN, zoom()); }
    private float toPageY(int screenY) { return PageCoordinateMapper.screenToPage(screenY, MARGIN, zoom()); }
    private Point anchor;
    private float oldX, oldY, oldW, oldH;
    private boolean resizing;
    private boolean dragStarted;
    private boolean editingBeforePress;
    private boolean clearedSelectionOnPress;
    private InlineNoteEditor inlineNote;
    /** Independent flowing body; never appears in the selectable placement collection. */
    private InlineNoteEditor inlineBody;
    private boolean forwardingBodyOverflow;
    private int lastBodyLength;
    private int bodyEditorPage = -1;
    private String editingId;
    private int liveEditorHeight;
    /** Start borderless page body editing. Floating notes remain separately selectable. */
    public void editBodyInline() { editBodyInlineAt(Float.NaN, Float.NaN); }
    public void editBodyInlineAt(float pageX, float pageY) {
        if (inlineBody != null) {
            positionBodyCaret(pageX, pageY);
            inlineBody.requestFocusInWindow(); return;
        }
        finishInlineEdit();
        final int editedPage = controller.currentPage();
        PdfDocument.Placement bodyNote = PdfBodyFlowLayout.displayNote(controller.body());
        inlineBody = new InlineNoteEditor(bodyNote, zoom(), spans -> {
            // Concatenate all downstream flowing pages before repagination; do not
            // discard text that was already on following pages.
            java.util.List<java.util.List<PdfDocument.NoteSpan>> rest = new java.util.ArrayList<>();
            rest.add(spans);
            for (int page = editedPage + 1; page < controller.pageCount(); page++)
                rest.add(controller.body(page));
            java.util.List<java.util.List<PdfDocument.NoteSpan>> pages =
                    PdfWrapTextLayout.paginate(PdfBodyFlowLayout.concat(rest),
                            controller.documentSnapshot(), editedPage);
            boolean differs = false;
            for (int i = editedPage; i < Math.max(controller.pageCount(), editedPage+pages.size()); i++) {
                var expected = i - editedPage < pages.size() ? pages.get(i-editedPage) : java.util.List.<PdfDocument.NoteSpan>of();
                var existing = i < controller.pageCount() ? controller.body(i) : java.util.List.<PdfDocument.NoteSpan>of();
                if (!expected.equals(existing)) { differs = true; break; }
            }
            if (differs) {
                controller.updateFlowingBody(editedPage, pages);
                listener.bodyChanged();
            }
            InlineNoteEditor old = inlineBody;
            inlineBody = null;
            if (old != null) remove(old);
            revalidate(); repaint();
        }, () -> checkBodyOverflow());
        bodyEditorPage = editedPage;
        lastBodyLength = inlineBody.getDocument().getLength();
        inlineBody.setBorder(null);
        inlineBody.setOpaque(false);
        inlineBody.setBackground(new Color(0,0,0,0));
        inlineBody.setMargin(new Insets(6, 7, 6, 7));
        inlineBody.setPassThroughHit(local -> {
            if (inlineBody == null) return false;
            float x = toPageX(inlineBody.getX() + local.x);
            float y = toPageY(inlineBody.getY() + local.y);
            for (PdfDocument.Placement material : controller.placements()) {
                if (material.isNote() || material.imageLayer() == PdfDocument.ImageLayer.BEHIND_TEXT) continue;
                if (x >= material.x() && x <= material.x()+material.width()
                        && y >= material.y() && y <= material.y()+material.height()) return true;
            }
            return false;
        });
        setLayout(null);
        positionBodyEditor();
        add(inlineBody, 0);
        revalidate(); repaint();
        positionBodyCaret(pageX, pageY);
        SwingUtilities.invokeLater(() -> { if (inlineBody != null) inlineBody.requestFocusInWindow(); });
    }
    private void positionBodyCaret(float pageX, float pageY) {
        if (inlineBody == null) return;
        int position;
        if (Float.isFinite(pageX) && Float.isFinite(pageY)) {
            position = inlineBody.viewToModel2D(new Point(
                    Math.max(0, Math.round((pageX - PdfDocument.BODY_LEFT) * (float)zoom())),
                    Math.max(0, Math.round((pageY - PdfDocument.BODY_TOP) * (float)zoom()))));
        } else position = inlineBody.getDocument().getLength();
        inlineBody.setCaretPosition(Math.max(0, Math.min(inlineBody.getDocument().getLength(), position)));
    }

    private void checkBodyOverflow() {
        if (inlineBody == null || forwardingBodyOverflow || controller.currentPage() != bodyEditorPage) return;
        int length = inlineBody.getDocument().getLength();
        // Visiting an already-filled earlier page or changing formatting must NOT
        // force a jump to the last page. Only actual new typing at the end may advance.
        boolean added = length > lastBodyLength;
        lastBodyLength = length;
        if (!added || inlineBody.getCaretPosition() < length - 2) return;
        java.util.List<PdfDocument.NoteSpan> content =
                pdf_editor.adapter.NoteTextDocumentFactory.toSpans(inlineBody, zoom());
        boolean overflowing = PdfWrapTextLayout.page(content, 0, controller.placements()).until()
                < PdfBodyFlowLayout.plainText(content).length();
        if (!overflowing) return;
        final int pageAtOverflow = bodyEditorPage;
        forwardingBodyOverflow = true;
        SwingUtilities.invokeLater(() -> {
            try {
                if (inlineBody != null && controller.currentPage() == pageAtOverflow) {
                    finishInlineEdit();
                    listener.bodyOverflow();
                }
            } finally { forwardingBodyOverflow = false; }
        });
    }

    private void positionBodyEditor() {
        if (inlineBody == null) return;
        int x = Math.round(PdfBodyFlowLayout.LEFT * (float) zoom()) + MARGIN;
        int y = Math.round(PdfBodyFlowLayout.TOP * (float) zoom()) + MARGIN;
        int width = Math.max(120, Math.round(PdfBodyFlowLayout.WIDTH * (float)zoom()));
        int height = Math.max(120, Math.round(PdfBodyFlowLayout.HEIGHT * (float)zoom()));
        inlineBody.setBounds(x, y, width, height);
    }
    /** Begin Word-like in-place editing of the selected text object, without opening another window. */
    public boolean editNoteInline() {
        PdfDocument.Placement note = controller.selected();
        if (note == null || !note.isNote()) return false;
        finishInlineEdit();
        editingId = note.id();
        liveEditorHeight = Math.max(42, Math.round(note.height() * (float) zoom()));
        inlineNote = new InlineNoteEditor(note, zoom(), spans -> {
            // Other mouse events may have moved the selection; target the original stable ID.
            String next = controller.selected() == null ? null : controller.selected().id();
            controller.select(editingId);
            if (controller.selected() != null) {
                controller.updateRichNote(spans);
                float desiredHeight = Math.max(note.height(), liveEditorHeight / (float) zoom());
                if (desiredHeight > note.height())
                    controller.transform(note.x(), note.y(), note.width(), desiredHeight);
            }
            controller.select(next);
            InlineNoteEditor old = inlineNote;
            inlineNote = null;
            editingId = null;
            if (old != null) remove(old);
            revalidate(); repaint();
        }, () -> growInlineNote());
        setLayout(null);
        positionInlineEditor(note);
        // The actual JTextPane is a Swing child above the whole page. While
        // editing a large body note, let clicks on higher PDF materials reach
        // this page instead of being swallowed by that child component.
        inlineNote.setPassThroughHit(local -> {
            InlineNoteEditor editing = inlineNote;
            return editing != null && materialAboveEditingNoteAt(
                    editing.getX() + local.x, editing.getY() + local.y);
        });
        add(inlineNote, 0);
        revalidate(); repaint();
        SwingUtilities.invokeLater(() -> { if (inlineNote != null) inlineNote.requestFocusInWindow(); });
        return true;
    }
    /** Only materials explicitly ABOVE the currently edited note get pointer priority. */
    private boolean materialAboveEditingNoteAt(int viewX, int viewY) {
        if (editingId == null) return false;
        List<PdfDocument.Placement> placements = controller.placements();
        int noteIndex = -1;
        for (int i = 0; i < placements.size(); i++) {
            if (editingId.equals(placements.get(i).id())) { noteIndex = i; break; }
        }
        if (noteIndex < 0) return false;
        float x = toPageX(viewX), y = toPageY(viewY);
        for (int i = placements.size() - 1; i > noteIndex; i--) {
            PdfDocument.Placement p = placements.get(i);
            if (!p.isNote() && x >= p.x() && x <= p.x() + p.width()
                    && y >= p.y() && y <= p.y() + p.height()) return true;
        }
        return false;
    }
    public boolean isEditingNote() { return inlineNote != null || inlineBody != null; }
    public void toggleInlineStyle(Object style) {
        InlineNoteEditor editing = inlineBody != null ? inlineBody : inlineNote;
        if (editing != null) editing.toggleStyle(style);
    }
    public void setInlineFontFamily(String family) {
        InlineNoteEditor editing = inlineBody != null ? inlineBody : inlineNote;
        if (editing != null) editing.fontFamily(family);
    }
    public void setInlineFontSize(int size) {
        InlineNoteEditor editing = inlineBody != null ? inlineBody : inlineNote;
        if (editing != null) editing.fontSize(size);
    }
    public void finishInlineEdit() {
        if (inlineBody != null) inlineBody.finish();
        // Re-measure synchronously before commit, even when a pending document event
        // has not yet reached the EDT (e.g., typing and immediately clicking away).
        if (inlineNote != null) growInlineNote();
        InlineNoteEditor editing = inlineNote;
        if (editing != null) editing.finish();
    }
    private void positionInlineEditor(PdfDocument.Placement note) {
        if (inlineNote == null) return;
        int x = Math.round(note.x() * (float)zoom()) + MARGIN;
        int y = Math.round(note.y() * (float)zoom()) + MARGIN;
        int w = Math.max(65, Math.round(note.width() * (float)zoom()));
        int h = Math.max(42, Math.round(note.height() * (float)zoom()));
        liveEditorHeight = Math.max(liveEditorHeight, h);
        inlineNote.setBounds(x, y, w, liveEditorHeight);
    }
    private void growInlineNote() {
        InlineNoteEditor editing = inlineNote;
        if (editing == null) return;
        // Measure with the actual content width; do not expand the visible component to 32K px.
        int desired = editing.getPreferredSize().height + 2;
        if (inlineNote != editing) return;  // focus may have committed this note during layout
        liveEditorHeight = Math.max(liveEditorHeight, Math.min(2200, desired));
        PdfDocument.Placement note = controller.placements().stream()
                .filter(p -> p.id().equals(editingId)).findFirst().orElse(null);
        if (note != null && inlineNote == editing) positionInlineEditor(note);
        if (inlineNote == editing) editing.repaint();
    }
    @Override public void doLayout() {
        super.doLayout();
        positionBodyEditor();
        if (inlineNote != null) {
            PdfDocument.Placement note = controller.selected();
            if (note != null && note.isNote()) positionInlineEditor(note);
        }
    }

    private boolean drawingOnPdf;
    private java.util.function.Supplier<view.DrawingCanvas.Tool> drawingTool = () -> view.DrawingCanvas.Tool.PEN;
    private java.util.function.Supplier<Color> drawingColor = () -> Color.BLACK;
    private java.util.function.Supplier<Float> drawingWidth = () -> 2.5f;
    private final java.util.List<java.awt.geom.Point2D.Float> inkPreview = new java.util.ArrayList<>();
    private final java.util.List<java.awt.geom.Point2D.Float> erasePreview = new java.util.ArrayList<>();
    /** Temporary raster previews only; commit the finished gesture once for a single Undo step. */
    private final java.util.Map<String, BufferedImage> liveErasedInk = new java.util.HashMap<>();
    private view.DrawingCanvas.Tool cursorTool;
    private Cursor pdfToolCursor;
    private java.awt.Point selectionBoxStart;
    private java.awt.Point selectionBoxEnd;
    private boolean selectingBox;
    // Match DrawingCanvas selection styling: same blue, translucent halo and dash cadence.
    private static final Color SELECTION_BLUE = new Color(37, 99, 235);
    private static final Color SELECTION_HALO = new Color(37, 99, 235, 60);
    private static final float[] SELECTION_DASH = {5f, 4f};

    private static BasicStroke selectionOutlineStroke() {
        return new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
                10f, SELECTION_DASH, 0f);
    }

    private final java.util.List<Point> lassoScreenPath = new java.util.ArrayList<>();
    private final java.util.Set<String> selectionCandidates = new java.util.LinkedHashSet<>();
    private final java.util.Map<String, PdfDocument.Placement> groupDragOriginal = new java.util.LinkedHashMap<>();
    /** Input routing is View-only; stored ink is an immutable PDF material placement. */
    public void configurePdfDrawing(boolean enabled,
                                    java.util.function.Supplier<view.DrawingCanvas.Tool> tool,
                                    java.util.function.Supplier<Color> color,
                                    java.util.function.Supplier<Float> width) {
        configurePdfDrawing(enabled, tool, color, width,
                () -> view.DrawingCanvas.EraserMode.PRECISE, () -> 16);
    }

    private java.util.function.Supplier<view.DrawingCanvas.EraserMode> eraserMode =
            () -> view.DrawingCanvas.EraserMode.PRECISE;
    private java.util.function.Supplier<Integer> eraserSize = () -> 16;
    public void configurePdfDrawing(boolean enabled,
                                    java.util.function.Supplier<view.DrawingCanvas.Tool> tool,
                                    java.util.function.Supplier<Color> color,
                                    java.util.function.Supplier<Float> width,
                                    java.util.function.Supplier<view.DrawingCanvas.EraserMode> mode,
                                    java.util.function.Supplier<Integer> size) {
        eraserMode = java.util.Objects.requireNonNull(mode);
        eraserSize = java.util.Objects.requireNonNull(size);
        drawingWidth = java.util.Objects.requireNonNull(width);
        drawingOnPdf = enabled;
        drawingTool = java.util.Objects.requireNonNull(tool);
        drawingColor = java.util.Objects.requireNonNull(color);
        inkPreview.clear(); erasePreview.clear(); liveErasedInk.clear();
        cursorTool = null;
        updateToolCursor();
        repaint();
    }
    public boolean isPdfDrawingEnabled() { return drawingOnPdf; }
    private boolean validPagePoint(java.awt.geom.Point2D.Float p) {
        return p.x >= 0 && p.y >= 0 && p.x <= PdfDocument.PAGE_WIDTH && p.y <= PdfDocument.PAGE_HEIGHT;
    }
    private java.awt.geom.Point2D.Float pagePoint(java.awt.Point screen) {
        return new java.awt.geom.Point2D.Float(toPageX(screen.x), toPageY(screen.y));
    }
    private boolean inkToolActive() {
        return drawingOnPdf && (drawingTool.get() == view.DrawingCanvas.Tool.PEN
                || drawingTool.get() == view.DrawingCanvas.Tool.ERASER);
    }
    private boolean selectionToolActive() {
        return drawingOnPdf && (drawingTool.get() == view.DrawingCanvas.Tool.LASSO
                || drawingTool.get() == view.DrawingCanvas.Tool.RECTANGLE);
    }
    /** Show provisional lasso membership before mouse release, not just the selection outline. */
    private void refreshSelectionCandidates() {
        selectionCandidates.clear();
        if (selectionBoxStart == null || selectionBoxEnd == null) return;
        java.awt.Shape region;
        if (drawingTool.get() == view.DrawingCanvas.Tool.LASSO && lassoScreenPath.size() > 2) {
            java.awt.geom.Path2D.Float path = new java.awt.geom.Path2D.Float();
            path.moveTo(lassoScreenPath.get(0).x, lassoScreenPath.get(0).y);
            for (int i = 1; i < lassoScreenPath.size(); i++)
                path.lineTo(lassoScreenPath.get(i).x, lassoScreenPath.get(i).y);
            path.closePath();
            region = path;
        } else {
            region = new Rectangle(Math.min(selectionBoxStart.x, selectionBoxEnd.x),
                    Math.min(selectionBoxStart.y, selectionBoxEnd.y),
                    Math.abs(selectionBoxStart.x - selectionBoxEnd.x),
                    Math.abs(selectionBoxStart.y - selectionBoxEnd.y));
        }
        for (PdfDocument.Placement item : controller.placements()) {
            int cx = Math.round((item.x() + item.width() / 2) * (float) zoom()) + MARGIN;
            int cy = Math.round((item.y() + item.height() / 2) * (float) zoom()) + MARGIN;
            int left = Math.round(item.x() * (float) zoom()) + MARGIN;
            int top = Math.round(item.y() * (float) zoom()) + MARGIN;
            int width = Math.max(1, Math.round(item.width() * (float) zoom()));
            int height = Math.max(1, Math.round(item.height() * (float) zoom()));
            if (region.contains(cx, cy) || region.intersects(left, top, width, height))
                selectionCandidates.add(item.id());
        }
    }
    private void updateToolCursor() {
        view.DrawingCanvas.Tool current = drawingOnPdf ? drawingTool.get() : null;
        if (cursorTool == current && pdfToolCursor != null) return;
        // A tool switch must not strand a previous lasso's blue selection overlay.
        if (cursorTool != current) {
            controller.clearSelection();
            selectionCandidates.clear();
            repaint();
        }
        cursorTool = current;
        pdfToolCursor = PdfToolCursors.forTool(current);
        setCursor(pdfToolCursor);
    }
    /** Escape deselects even when the view isn't redrawn by a document operation. */
    public void clearPageSelection() {
        controller.clearSelection();
        selectionCandidates.clear();
        groupDragOriginal.clear();
        selectingBox = false;
        selectionBoxStart = null; selectionBoxEnd = null;
        lassoScreenPath.clear();
        anchor = null;
        repaint();
    }
    private void refreshErasePreview() {
        liveErasedInk.clear();
        if (erasePreview.isEmpty()) return;
        float radius = eraserSize.get() / 2f;
        for (PdfDocument.Placement placement : controller.placements()) {
            if (!isPdfInk(placement) || !PdfInkRasterEraser.touches(placement, erasePreview, radius)) continue;
            if (eraserMode.get() == view.DrawingCanvas.EraserMode.STROKE) {
                liveErasedInk.put(placement.id(), new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
            } else {
                BufferedImage image = PdfInkRasterEraser.erase(placement, erasePreview, radius);
                if (image != null) liveErasedInk.put(placement.id(), image);
            }
        }
    }
    private void beginPdfStroke(MouseEvent e) {
        inkPreview.clear(); erasePreview.clear(); liveErasedInk.clear();
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        java.awt.geom.Point2D.Float p = pagePoint(e.getPoint());
        if (!validPagePoint(p)) return;
        if (drawingTool.get() == view.DrawingCanvas.Tool.ERASER) {
            erasePreview.add(p);
            refreshErasePreview();
            repaint();
        } else if (drawingTool.get() == view.DrawingCanvas.Tool.PEN) {
            inkPreview.add(p);
        }
    }
    private void continuePdfStroke(MouseEvent e) {
        java.awt.geom.Point2D.Float p = pagePoint(e.getPoint());
        if (!validPagePoint(p)) return;
        if (!inkPreview.isEmpty()) inkPreview.add(p);
        if (!erasePreview.isEmpty()) {
            erasePreview.add(p);
            refreshErasePreview();
        }
        repaint();
    }
    private void completePdfEraser() {
        if (erasePreview.isEmpty()) return;
        java.util.List<java.awt.geom.Point2D.Float> path = java.util.List.copyOf(erasePreview);
        erasePreview.clear(); liveErasedInk.clear();
        if (eraserMode.get() == view.DrawingCanvas.EraserMode.STROKE) {
            // Erase only PDF-origin ink strokes, never formulas or other library items.
            for (var placement : java.util.List.copyOf(controller.placements())) {
                if (!isPdfInk(placement) || !PdfInkRasterEraser.touches(placement, path, eraserSize.get() / 2f)) continue;
                controller.select(placement.id()); controller.deleteSelected();
            }
        } else {
            java.util.Map<String, BufferedImage> edits = new java.util.LinkedHashMap<>();
            for (var placement : controller.placements()) {
                if (!isPdfInk(placement) || !PdfInkRasterEraser.touches(placement, path, eraserSize.get() / 2f)) continue;
                BufferedImage result = PdfInkRasterEraser.erase(placement, path, eraserSize.get() / 2f);
                if (result != null) edits.put(placement.id(), result);
            }
            controller.replaceInkImages(edits);
        }
        repaint();
    }
    private static boolean isPdfInk(PdfDocument.Placement placement) {
        return !placement.isNote() && placement.material().isGraph()
                && "PDF ink stroke".equals(placement.material().title());
    }
    private void completePdfStroke() {
        if (inkPreview.isEmpty()) return;
        float minX=Float.MAX_VALUE,minY=Float.MAX_VALUE,maxX=-Float.MAX_VALUE,maxY=-Float.MAX_VALUE;
        for (var p : inkPreview) {minX=Math.min(minX,p.x);minY=Math.min(minY,p.y);
            maxX=Math.max(maxX,p.x);maxY=Math.max(maxY,p.y);}
        float pad=5f;
        float x=Math.max(0,minX-pad), y=Math.max(0,minY-pad);
        float w=Math.min(PdfDocument.PAGE_WIDTH-x, Math.max(10,maxX-x+pad));
        float h=Math.min(PdfDocument.PAGE_HEIGHT-y, Math.max(10,maxY-y+pad));
        int quality=3;
        BufferedImage image=new BufferedImage(Math.max(1,(int)Math.ceil(w*quality)),
                Math.max(1,(int)Math.ceil(h*quality)),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=image.createGraphics();
        try {
            g.scale(quality,quality);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(Math.max(1f, drawingWidth.get()),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            g.setColor(drawingColor.get());
            java.awt.geom.Path2D.Float path=new java.awt.geom.Path2D.Float();
            boolean first=true;
            for (var point : inkPreview) {if(first){path.moveTo(point.x-x,point.y-y);first=false;}
                else path.lineTo(point.x-x,point.y-y);}
            g.draw(path);
            if(inkPreview.size()==1)g.fillOval(Math.round(inkPreview.get(0).x-x)-1,
                    Math.round(inkPreview.get(0).y-y)-1,3,3);
        } finally {g.dispose();}
        controller.place(MathLibrary.region("PDF ink stroke",image),x,y,w,h);
        controller.clearSelection();
        inkPreview.clear();repaint();
    }

    public PdfPageCanvas(PdfEditorController controller, Listener listener) {
        this.controller = Objects.requireNonNull(controller);
        this.listener = Objects.requireNonNull(listener);
        updateSize();
        setFocusable(true);
        setBackground(new Color(243, 247, 253));
        setTransferHandler(new TransferHandler() {
            @Override public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(CanvasPdfDragController.HISTORY_SELECTION_FLAVOR)
                        || support.isDataFlavorSupported(DataFlavor.stringFlavor)
                        || support.isDataFlavorSupported(DataFlavor.imageFlavor);
            }
            @Override public boolean importData(TransferSupport support) {
                if (!canImport(support)) return false;
                try {
                    Point location = support.getDropLocation().getDropPoint();
                    // A live inline note editor is a Swing child covering the page.
                    // Commit/remove it before inserting the dropped History material.
                    finishInlineEdit();
                    if (support.isDataFlavorSupported(CanvasPdfDragController.HISTORY_SELECTION_FLAVOR)) {
                        CanvasHistorySelection selection = (CanvasHistorySelection) support.getTransferable()
                                .getTransferData(CanvasPdfDragController.HISTORY_SELECTION_FLAVOR);
                        CanvasHistoryImporter.importAt(selection, controller,
                                toPageX(location.x), toPageY(location.y));
                        repaint();
                        return true;
                    }
                    if (support.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                        java.awt.Image source = (java.awt.Image) support.getTransferable()
                                .getTransferData(DataFlavor.imageFlavor);
                        BufferedImage image = imageAsBuffered(source);
                        MathLibrary.Material region = controller.library().addInk("Canvas selection", image);
                        float width = Math.min(440, Math.max(85, image.getWidth()));
                        float height = width * image.getHeight() / Math.max(1f, image.getWidth());
                        if (height > 575) { width *= 575f / height; height = 575f; }
                        controller.place(region, toPageX(location.x), toPageY(location.y), width, height);
                        repaint(); return true;
                    }
                    String id = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    MathLibrary.Material material = controller.library().find(id);
                    if (material == null) return false;
                    float itemWidth = material.isGraph() ? 260f : 300f;
                    float itemHeight = material.isGraph()
                            ? Math.max(25f, itemWidth * material.imageHeight() / Math.max(1f, material.imageWidth()))
                            : 100f;
                    if (itemHeight > 575f) { itemWidth *= 575f / itemHeight; itemHeight = 575f; }
                    controller.place(material, toPageX(location.x), toPageY(location.y), itemWidth, itemHeight);
                    repaint(); return true;
                } catch (Exception ignored) { return false; }
            }
        });
        MouseAdapter mouse = new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { updateToolCursor(); }
            @Override public void mouseMoved(MouseEvent e) { updateToolCursor(); }
            @Override public void mousePressed(MouseEvent e) {
                updateToolCursor();
                requestFocusInWindow();
                if (inkToolActive()) {beginPdfStroke(e);return;}
                // Commit an active body/note before choosing a PDF object.
                editingBeforePress = inlineNote != null || inlineBody != null;
                finishInlineEdit();
                clearedSelectionOnPress = !controller.selectedIds().isEmpty();
                PdfDocument.Placement hit = controller.hitAt(toPageX(e.getX()), toPageY(e.getY()));
                boolean additive = e.isShiftDown() || e.isControlDown() || e.isMetaDown();
                anchor = null; dragStarted = false;
                selectingBox = selectionToolActive() && hit == null && SwingUtilities.isLeftMouseButton(e);
                selectionBoxStart = selectingBox ? e.getPoint() : null;
                selectionBoxEnd = selectionBoxStart;
                lassoScreenPath.clear();
                groupDragOriginal.clear();
                if (selectingBox) {
                    selectionCandidates.clear();
                    lassoScreenPath.add(e.getPoint());
                    if (!additive) controller.clearSelection();
                } else if (hit == null) {
                    controller.clearSelection();
                } else if (additive && SwingUtilities.isLeftMouseButton(e)) {
                    controller.toggleSelection(hit.id());
                } else if (!controller.isSelected(hit.id())) {
                    controller.select(hit.id());
                }
                PdfDocument.Placement p = controller.selected();
                // Preserve the entire selected group when starting a drag on any member.
                if (hit != null && controller.isSelected(hit.id()) && !additive) {
                    for (var element : controller.placements()) {
                        if (controller.isSelected(element.id())) groupDragOriginal.put(element.id(), element);
                    }
                    p = hit;
                }
                if (p != null && !additive) {
                    oldX = p.x(); oldY = p.y(); oldW = p.width(); oldH = p.height();
                    resizing = controller.selectedIds().size() == 1 &&
                            toPageX(e.getX()) > p.x() + p.width() - 18 / zoom()
                            && toPageY(e.getY()) > p.y() + p.height() - 18 / zoom();
                    if (SwingUtilities.isLeftMouseButton(e)) anchor = e.getPoint();
                }
                if (e.isPopupTrigger()) listener.contextMenu(e);
                repaint();
            }
            @Override public void mouseDragged(MouseEvent e) {
                updateToolCursor();
                if (inkToolActive()) {continuePdfStroke(e);return;}
                if (selectingBox) {
                    selectionBoxEnd = e.getPoint();
                    lassoScreenPath.add(e.getPoint());
                    refreshSelectionCandidates();
                    repaint(); return;
                }
                if (controller.selected() == null || anchor == null) return;
                float dx = PageCoordinateMapper.deltaToPage(e.getX() - anchor.x, zoom());
                float dy = PageCoordinateMapper.deltaToPage(e.getY() - anchor.y, zoom());
                if (!dragStarted) {
                    if (dx == 0 && dy == 0) return;
                    if (groupDragOriginal.size() > 1) controller.beginGroupTransform(groupDragOriginal.keySet());
                    else controller.beginTransform();
                    dragStarted = true;
                }
                if (groupDragOriginal.size() > 1) {
                    controller.transformGroup(groupDragOriginal, dx, dy);
                } else if (resizing) controller.transform(oldX, oldY, oldW + dx, oldH + dy);
                else controller.transform(oldX + dx, oldY + dy, oldW, oldH);
                repaint();
            }
            @Override public void mouseReleased(MouseEvent e) {
                if (inkToolActive()) {
                    if (drawingTool.get() == view.DrawingCanvas.Tool.ERASER) completePdfEraser();
                    else completePdfStroke();
                    return;
                }
                if (selectingBox) {
                    selectingBox = false;
                    if (selectionBoxStart != null) {
                        selectionBoxEnd = e.getPoint();
                        java.awt.Shape region;
                        if (drawingTool.get() == view.DrawingCanvas.Tool.LASSO && lassoScreenPath.size() >= 3) {
                            java.awt.geom.Path2D.Float outline = new java.awt.geom.Path2D.Float();
                            outline.moveTo(lassoScreenPath.get(0).x, lassoScreenPath.get(0).y);
                            for (int i = 1; i < lassoScreenPath.size(); i++)
                                outline.lineTo(lassoScreenPath.get(i).x, lassoScreenPath.get(i).y);
                            outline.closePath();
                            region = outline;
                        } else {
                            region = new Rectangle(Math.min(selectionBoxStart.x, selectionBoxEnd.x),
                                    Math.min(selectionBoxStart.y, selectionBoxEnd.y),
                                    Math.abs(selectionBoxStart.x - selectionBoxEnd.x),
                                    Math.abs(selectionBoxStart.y - selectionBoxEnd.y));
                        }
                        java.util.Set<String> chosen = new java.util.LinkedHashSet<>();
                        for (var item : controller.placements()) {
                            int cx = Math.round((item.x() + item.width()/2) * (float)zoom()) + MARGIN;
                            int cy = Math.round((item.y() + item.height()/2) * (float)zoom()) + MARGIN;
                            int left = Math.round(item.x() * (float) zoom()) + MARGIN;
                            int top = Math.round(item.y() * (float) zoom()) + MARGIN;
                            int width = Math.max(1, Math.round(item.width() * (float) zoom()));
                            int height = Math.max(1, Math.round(item.height() * (float) zoom()));
                            if (region.contains(cx, cy) || region.intersects(left, top, width, height))
                                chosen.add(item.id());
                        }
                        controller.setSelection(chosen, e.isShiftDown() || e.isControlDown() || e.isMetaDown());
                    }
                    // Keep multi-selection visible until Escape, blank click or tool/page switch.
                    selectionCandidates.clear();
                    selectionBoxStart = null; selectionBoxEnd = null; lassoScreenPath.clear();
                    repaint();
                }
                boolean movedPicture = dragStarted;
                anchor = null; dragStarted = false; groupDragOriginal.clear();
                if (movedPicture && controller.reflowAfterImageMove()) listener.bodyChanged();
                if (e.isPopupTrigger()) listener.contextMenu(e);
            }
            @Override public void mouseClicked(MouseEvent e) {
                if (inkToolActive() || selectionToolActive()) return;
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1 && !editingBeforePress
                        && !clearedSelectionOnPress && !e.isShiftDown() && !e.isControlDown() && !e.isMetaDown()) {
                    var chosen = controller.selected();
                    float x=toPageX(e.getX()), y=toPageY(e.getY());
                    if (x>=0 && y>=0 && x<PdfDocument.PAGE_WIDTH && y<PdfDocument.PAGE_HEIGHT) {
                        if (chosen == null) listener.typeInDocumentAt(x,y);
                        else if (chosen.isNote()) listener.editNote();
                    }
                }
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    if (controller.selected() == null) {
                        listener.typeInDocumentAt(toPageX(e.getX()), toPageY(e.getY()));
                    } else if (controller.selected().isNote()) listener.editNote();
                    else if (!controller.selected().material().isGraph()) listener.editFormula();
                }
                clearedSelectionOnPress = false;
            }
        };
        addMouseListener(mouse); addMouseMotionListener(mouse);
        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ESCAPE"), "pdfClearSelection");
        getActionMap().put("pdfClearSelection", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { clearPageSelection(); }
        });
    }
    private static java.awt.image.BufferedImage imageAsBuffered(java.awt.Image image) {
        if (image instanceof java.awt.image.BufferedImage buffered) return buffered;
        if (image == null || image.getWidth(null) < 1 || image.getHeight(null) < 1)
            throw new IllegalArgumentException("Unsupported canvas image");
        java.awt.image.BufferedImage copy = new java.awt.image.BufferedImage(image.getWidth(null),
                image.getHeight(null), java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D graphics = copy.createGraphics();
        graphics.drawImage(image, 0, 0, null); graphics.dispose();
        return copy;
    }

    @Override protected void paintChildren(Graphics graphics) {
        super.paintChildren(graphics);
        if (inlineBody == null) return;
        // Swing children normally paint above the parent; redraw just the
        // foreground media so text typed behind a picture cannot cover it.
        Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.translate(MARGIN, MARGIN);
            g.scale(zoom(), zoom());
            g.translate(-MARGIN, -MARGIN);
            for (PdfDocument.Placement p : controller.placements())
                if (!p.isNote() && p.imageLayer() != PdfDocument.ImageLayer.BEHIND_TEXT)
                    paintPlacement(g, p);
        } finally { g.dispose(); }
    }

    private void paintPlacement(Graphics2D g, PdfDocument.Placement p) {
        int x = Math.round(p.x()) + MARGIN, y = Math.round(p.y()) + MARGIN;
        int width = Math.round(p.width()), height = Math.round(p.height());
        double deviceScale = Math.max(1.0, zoom() * Math.max(1.0,
                g.getDeviceConfiguration().getDefaultTransform().getScaleX()));
        int w = Math.min(3200, Math.max(8, (int)Math.ceil(width * deviceScale)));
        int h = Math.min(3200, Math.max(8, (int)Math.ceil(height * deviceScale)));
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        BufferedImage preview = liveErasedInk.get(p.id());
        if (preview == null) g.drawImage(MaterialRenderer.render(p.material(), w, h), x, y, width, height, null);
        else g.drawImage(preview, x, y, width, height, null);
        // Keep selection visible on behind-text objects too.
        if (controller.isSelected(p.id()) || selectionCandidates.contains(p.id())) {
            g.setColor(SELECTION_HALO); g.fillRoundRect(x,y,width,height,8,8);
            g.setColor(SELECTION_BLUE); g.setStroke(selectionOutlineStroke());
            g.drawRoundRect(x,y,width,height,8,8);
            g.setStroke(new BasicStroke(1.5f));
        }
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            java.awt.geom.AffineTransform screenTransform = g2.getTransform();
            // Page and placements share the same physical view transform.
            g2.translate(MARGIN, MARGIN);
            g2.scale(zoom(), zoom());
            g2.translate(-MARGIN, -MARGIN);
            g2.setColor(new Color(117, 142, 173, 28));
            g2.fillRoundRect(MARGIN + 5, MARGIN + 7, (int)PdfDocument.PAGE_WIDTH,
                    (int)PdfDocument.PAGE_HEIGHT, 5, 5);
            g2.setColor(Color.WHITE);
            g2.fillRect(MARGIN, MARGIN, (int)PdfDocument.PAGE_WIDTH, (int)PdfDocument.PAGE_HEIGHT);
            g2.setColor(new Color(215, 225, 238));
            g2.drawRect(MARGIN, MARGIN, (int)PdfDocument.PAGE_WIDTH, (int)PdfDocument.PAGE_HEIGHT);
            PdfDocument.Placement selected = controller.selected();
            List<PdfDocument.Placement> placements = controller.placements();
            // First render images below text, then the independent body, then objects above.
            for (PdfDocument.Placement p : placements) {
                if (!p.isNote() && p.imageLayer() == PdfDocument.ImageLayer.BEHIND_TEXT)
                    paintPlacement(g2, p);
            }
            if (inlineBody == null && !controller.body().isEmpty()) {
                var body = PdfBodyFlowLayout.displayNote(controller.body());
                double deviceScale = Math.max(1.0, zoom() * Math.max(1.0,
                        g2.getDeviceConfiguration().getDefaultTransform().getScaleX()));
                int w = Math.min(3200, (int)Math.ceil(body.width()*deviceScale));
                int h = Math.min(3200, (int)Math.ceil(body.height()*deviceScale));
                g2.drawImage(PdfWrapTextLayout.render(controller.body(), placements, w, h),
                        Math.round(body.x())+MARGIN, Math.round(body.y())+MARGIN,
                        Math.round(body.width()), Math.round(body.height()), null);
            }
            for (PdfDocument.Placement p : placements) {
                if (!p.isNote() && p.imageLayer() == PdfDocument.ImageLayer.BEHIND_TEXT) continue;
                if (inlineBody != null && !p.isNote()) continue;
                if (inlineNote != null && p.id().equals(editingId)) continue;
                int x = Math.round(p.x()) + MARGIN, y = Math.round(p.y()) + MARGIN;
                int width = Math.round(p.width()), height = Math.round(p.height());
                // Render at device-visible resolution on every paint/zoom: a single
                // low-res bitmap must never be stretched when the user zooms in.
                double deviceScale = Math.max(1.0, zoom() * Math.max(1.0,
                        g2.getDeviceConfiguration().getDefaultTransform().getScaleX()));
                int rasterWidth = Math.min(3200, Math.max(8, (int) Math.ceil(width * deviceScale)));
                int rasterHeight = Math.min(3200, Math.max(8, (int) Math.ceil(height * deviceScale)));
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                BufferedImage preview = liveErasedInk.get(p.id());
                if (preview == null) {
                    g2.drawImage(p.isNote() ? NoteRenderer.render(p, rasterWidth, rasterHeight)
                            : MaterialRenderer.render(p.material(), rasterWidth, rasterHeight),
                            x, y, width, height, null);
                } else if (preview.getWidth() > 1 || preview.getHeight() > 1) {
                    g2.drawImage(preview, x, y, width, height, null);
                }
                if (controller.isSelected(p.id()) || selectionCandidates.contains(p.id())) {
                    g2.setColor(SELECTION_HALO);
                    g2.fillRoundRect(x, y, width, height, 8, 8);
                    g2.setColor(SELECTION_BLUE);
                    g2.setStroke(selectionOutlineStroke());
                    g2.drawRoundRect(x, y, width, height, 8, 8);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.setColor(Color.WHITE);
                    g2.fillRect(x + width - 9, y + height - 9, 9, 9);
                    g2.setColor(SELECTION_BLUE);
                    g2.drawRect(x + width - 9, y + height - 9, 9, 9);
                }
            }
            if (!inkPreview.isEmpty()) {
                g2.setStroke(new BasicStroke(Math.max(1f, drawingWidth.get()), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(drawingColor.get());
                java.awt.geom.Path2D.Float path = new java.awt.geom.Path2D.Float();
                boolean first = true;
                for (var point : inkPreview) {
                    if (first) {path.moveTo(point.x + MARGIN, point.y + MARGIN);first=false;}
                    else path.lineTo(point.x + MARGIN, point.y + MARGIN);
                }
                g2.draw(path);
            }
            // The lasso overlay MUST be rendered above the opaque PDF page.
            // Painting it before the page background made its blue fill invisible.
            g2.setTransform(screenTransform);
            if (selectingBox && selectionBoxStart != null && selectionBoxEnd != null) {
                int rx = Math.min(selectionBoxStart.x, selectionBoxEnd.x);
                int ry = Math.min(selectionBoxStart.y, selectionBoxEnd.y);
                int rw = Math.abs(selectionBoxStart.x - selectionBoxEnd.x);
                int rh = Math.abs(selectionBoxStart.y - selectionBoxEnd.y);
                if (selectionToolActive() && drawingTool.get() == view.DrawingCanvas.Tool.LASSO
                        && lassoScreenPath.size() >= 2) {
                    java.awt.geom.Path2D.Float outline = new java.awt.geom.Path2D.Float();
                    outline.moveTo(lassoScreenPath.get(0).x, lassoScreenPath.get(0).y);
                    for (int i = 1; i < lassoScreenPath.size(); i++)
                        outline.lineTo(lassoScreenPath.get(i).x, lassoScreenPath.get(i).y);
                    // Match Canvas: close the filled region, then draw a thin dashed blue boundary.
                    java.awt.geom.Path2D.Float fill = (java.awt.geom.Path2D.Float) outline.clone();
                    fill.closePath();
                    g2.setColor(SELECTION_HALO);
                    g2.fill(fill);
                    g2.setColor(SELECTION_BLUE);
                    g2.setStroke(selectionOutlineStroke());
                    g2.draw(fill);
                } else {
                    g2.setColor(SELECTION_HALO);
                    g2.fillRoundRect(rx, ry, rw, rh, 8, 8);
                    g2.setColor(SELECTION_BLUE);
                    g2.setStroke(selectionOutlineStroke());
                    g2.drawRoundRect(rx, ry, rw, rh, 8, 8);
                }
            }
        } finally { g2.dispose(); }
    }
}
