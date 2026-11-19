package app;

import data_access.InMemoryFunctionDataAccessObject;
import history.data_access.FileHistoryRepository;
import history.use_case.HistoryInteractor;
import history.adapter.HistoryBoardPresenter;
import view.board.PdfPanel;
import pdf_editor.view.CanvasPdfDragController;
import pdf_editor.view.CanvasRegionCaptureController;
import pdf_editor.view.CanvasLibrarySelectionMenu;
import pdf_editor.adapter.SelectedInkRegistry;
import pdf_editor.use_case.SelectionToLibrary;
import data_access.InMemoryGraphSessionDataAccessObject;
import entity.LatexFunctionParser;
import interface_adapter.MathLabViewModel;
import interface_adapter.evaluate.CalculateCheckController;
import interface_adapter.evaluate.DefinitionCheckController;
import interface_adapter.evaluate.EvaluateController;
import interface_adapter.evaluate.EvaluatePresenter;
import interface_adapter.evaluate.QuestionCheckController;
import interface_adapter.graph.GraphController;
import interface_adapter.graph.GraphExploreController;
import interface_adapter.graph.GraphPresenter;
import interface_adapter.graph.GraphState;
import interface_adapter.graph.GraphViewModel;
import use_case.explore_graph.ExploreGraphInteractor;
import view.graph.GraphSnapshot;
import view.canvas.GraphSession;
import view.canvas.PhaseSession;
import interface_adapter.graph_check.GraphCheckController;
import interface_adapter.history.HistoryViewModel;
import interface_adapter.recognize.FormulaRecognizer;
import pdf_editor.PdfStudioLauncher;
import use_case.evaluate.CalculateCheckInteractor;
import use_case.evaluate.DefinitionCheckInteractor;
import use_case.evaluate.EvaluateInteractor;
import use_case.evaluate.QuestionCheckInteractor;
import use_case.graph_check.GraphCheckInteractor;
import use_case.phase_check.PhaseCheckInteractor;
import use_case.phase_portrait.PhasePortraitInteractor;
import use_case.explore_phase.ExplorePhaseInteractor;
import entity.phase.DifferentialEquationParser;
import data_access.InMemoryPhaseSessionDataAccessObject;
import interface_adapter.phase.PhaseCheckController;
import interface_adapter.phase.PhaseController;
import interface_adapter.phase.PhaseExploreController;
import interface_adapter.phase.PhasePresenter;
import interface_adapter.phase.PhaseState;
import interface_adapter.phase.PhaseViewModel;
import view.graph.PhaseSnapshot;
import use_case.graph_function.GraphFunctionInteractor;
import view.DrawingCanvas;
import view.board.BoardTheme;
import view.board.CanvasWorkspaceDialogs;
import workspace.use_case.CanvasWorkspace;
import workspace.data_access.FileCanvasRepository;
import pdf_editor.view.CanvasHistoryEditMenu;
import view.board.BoardWindow;
import view.board.FloatingToolbar;
import view.board.HistoryPanel;
import view.canvas.AnswerCanvasPage;
import view.canvas.SharedCanvas;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

/** Builds the one-board window: canvas, History and PDF panels, popup with Calculate and Graph. */
public class StackedAppBuilder {
    private final DrawingCanvas drawing = new DrawingCanvas();
    private final SharedCanvas sharedCanvas = new SharedCanvas(drawing);
    private final HistoryInteractor history = new HistoryInteractor(
            new FileHistoryRepository(FileHistoryRepository.defaultDirectory()));
    private final PdfStudioLauncher pdfStudio = new PdfStudioLauncher(history);
    private final CanvasWorkspace canvases = new CanvasWorkspace(
            new FileCanvasRepository(FileCanvasRepository.defaultDirectory()));

    {
        pdfStudio.setSourceCanvas(sharedCanvas);
        sharedCanvas.addFormulaChangeListener(id -> pdfStudio.synchronizeCanvas(sharedCanvas));
        drawing.addInkChangeListener(() -> SwingUtilities.invokeLater(
                () -> pdfStudio.synchronizeCanvas(sharedCanvas)));
        sharedCanvas.addUserActionListener(() -> SwingUtilities.invokeLater(
                () -> pdfStudio.synchronizeCanvas(sharedCanvas)));
    }

    public StackedAppBuilder setFormulaRecognizer(FormulaRecognizer recognizer) {
        sharedCanvas.setFormulaRecognizer(recognizer);
        return this;
    }

    public SharedCanvas getSharedCanvas() {
        return sharedCanvas;
    }

    public JFrame build() {
        // evaluate
        MathLabViewModel mathViewModel = new MathLabViewModel();
        InMemoryFunctionDataAccessObject functions = new InMemoryFunctionDataAccessObject();
        EvaluateController evaluate = new EvaluateController(new EvaluateInteractor(
                functions, new EvaluatePresenter(mathViewModel)));
        CalculateCheckController calculateCheck = new CalculateCheckController(new CalculateCheckInteractor(functions));
        DefinitionCheckController definitionCheck = new DefinitionCheckController(new DefinitionCheckInteractor());
        QuestionCheckController questionCheck = new QuestionCheckController(new QuestionCheckInteractor());

        // every graph on the canvas gets its own session (graphing + exploring share it), so several
        // stay live at once; each new graph also goes to PDF Studio like before
        java.util.function.Supplier<GraphSession> graphSessions = () -> {
            GraphViewModel graphViewModel = new GraphViewModel();
            graphViewModel.addPropertyChangeListener(event -> {
                // only brand new graphs, pans / zooms come as VIEW and would spam the library
                if (GraphViewModel.STATE_PROPERTY.equals(event.getPropertyName())) {
                    GraphState state = graphViewModel.getState();
                    if (state.getError() == null && state.hasGraph()) {
                        pdfStudio.addGraph(state.getLatex(), GraphSnapshot.render(state, 880, 600));
                    }
                }
            });
            InMemoryGraphSessionDataAccessObject graphSession = new InMemoryGraphSessionDataAccessObject();
            GraphPresenter graphPresenter = new GraphPresenter(graphViewModel);
            return new GraphSession(
                    new GraphController(new GraphFunctionInteractor(new LatexFunctionParser(), graphSession,
                            graphPresenter)),
                    new GraphExploreController(new ExploreGraphInteractor(graphSession, graphPresenter)),
                    graphViewModel);
        };
        GraphCheckController graphCheck = new GraphCheckController(new GraphCheckInteractor(new LatexFunctionParser()));

        // phase portraits of odes: own use case, a session per portrait, same popup; also go to PDF Studio
        java.util.function.Supplier<PhaseSession> phaseSessions = () -> {
            PhaseViewModel phaseViewModel = new PhaseViewModel();
            phaseViewModel.addPropertyChangeListener(event -> {
                if (PhaseViewModel.STATE_PROPERTY.equals(event.getPropertyName())) {
                    PhaseState state = phaseViewModel.getState();
                    if (state.getError() == null && state.hasPortrait()) {
                        pdfStudio.addGraph(state.getLatex(), PhaseSnapshot.render(state, 880, 600));
                    }
                }
            });
            InMemoryPhaseSessionDataAccessObject phaseSession = new InMemoryPhaseSessionDataAccessObject();
            PhasePresenter phasePresenter = new PhasePresenter(phaseViewModel);
            return new PhaseSession(
                    new PhaseController(new PhasePortraitInteractor(new DifferentialEquationParser(), phaseSession,
                            phasePresenter)),
                    new PhaseExploreController(new ExplorePhaseInteractor(phaseSession, phasePresenter)),
                    phaseViewModel);
        };
        PhaseCheckController phaseCheck = new PhaseCheckController(
                new PhaseCheckInteractor(new DifferentialEquationParser()));

        // history
        HistoryViewModel historyViewModel = new HistoryViewModel();
        HistoryBoardPresenter historyPresenter = new HistoryBoardPresenter(history, historyViewModel);
        historyPresenter.refresh();

        AnswerCanvasPage.FormulaResultSink results = (id, latex, result) -> {
            pdfStudio.addCalculationWithSource(id, latex, result);
            // MathLabCollector persists this calculation when it receives the same event.
        };
        drawing.setBackground(BoardTheme.CANVAS);
        drawing.setGridVisible(false);
        drawing.setMathAssetRenderer((latex, result, width, height) ->
                pdf_editor.data_access.MaterialRenderer.render(
                        new pdf_editor.domain.PdfMaterial("Canvas History", latex, result, null, 0, 0) { },
                        width, height));
        CanvasPdfDragController.install(drawing);
        // Reuse the existing US2 evaluation use case with a dedicated view model, so
        // editing History does not race with the Canvas answer worker's view-model updates.
        MathLabViewModel historyEditModel = new MathLabViewModel();
        pdf_editor.adapter.HistoryFormulaCalculator historyEditCalculator =
                new pdf_editor.adapter.HistoryFormulaCalculator(
                        new EvaluateController(new EvaluateInteractor(
                                functions, new EvaluatePresenter(historyEditModel))), historyEditModel);
        pdfStudio.setFormulaRecalculator(historyEditCalculator::calculate);
        CanvasHistoryEditMenu.install(drawing, (latex, result) ->
                pdf_editor.data_access.MaterialRenderer.render(
                        new pdf_editor.domain.PdfMaterial("Edited Math", latex, result, null, 0, 0) { },
                        800, 190), historyEditCalculator::calculate,
                (latex, result) -> {
                    if (latex.isBlank()) return;
                    if (result.isBlank()) pdfStudio.getLibrary().addFormula(latex);
                    else pdfStudio.getLibrary().addCalculation(latex, result);
                }, change -> {
                    try {
                        pdfStudio.reviseCanvasHistory(change.oldFormula(), change.oldResult(),
                                change.formula(), change.result());
                    } catch (java.io.IOException failure) {
                        System.err.println("Formula revision sync failed: " + failure.getMessage());
                    }
                });
        CanvasLibrarySelectionMenu.install(drawing,
                new SelectionToLibrary(sharedCanvas::recognizeFormula, pdfStudio.getLibrary()),
                new SelectedInkRegistry(drawing, pdfStudio.getLibrary()));
        AnswerCanvasPage page = new AnswerCanvasPage(sharedCanvas, evaluate, mathViewModel, results,
                graphSessions, graphCheck::canGraph, BoardTheme.CANVAS);
        page.setDefinitionCheck(definitionCheck::isDefinition);
        page.setCalculateCheck(calculateCheck::canCalculate);
        page.setQuestionCheck(questionCheck::isQuestion);
        page.setFaintEqualsCheck(calculateCheck::canCalculate);
        page.enablePhasePortrait(phaseSessions, phaseCheck::isDifferentialEquation);
        sharedCanvas.attachTo(page);
        PdfPanel pdfPanel = pdfStudio.createPanel();
        // Use the live selection (lasso or whole ink), not an outdated startup screenshot.
        pdfPanel.setCanvasCapture(() -> {
            try { return drawing.toFormulaImage(); }
            catch (RuntimeException empty) { return null; }
        });
        java.util.concurrent.atomic.AtomicReference<Runnable> cancelCapture = new java.util.concurrent.atomic.AtomicReference<>();
        pdfPanel.setRegionCapture(() -> {
            if (cancelCapture.get() != null) {
                cancelCapture.getAndSet(null).run();
                return;
            }
            pdfPanel.setCaptureAreaActive(true);
            Runnable close = CanvasRegionCaptureController.begin(
                    page.boardArea(), drawing, image -> {
                        pdfStudio.getLibrary().addInk("Canvas screenshot", image);
                        pdfPanel.editor().showLibrary();
                    }, () -> {
                        cancelCapture.set(null);
                        pdfPanel.setCaptureAreaActive(false);
                    });
            cancelCapture.set(close);
        });
        // Save/Open are separate use-case commands; the board knows only the callbacks.
        java.util.concurrent.atomic.AtomicReference<CanvasWorkspaceDialogs> dialogs = new java.util.concurrent.atomic.AtomicReference<>();
        BoardWindow board = new BoardWindow(page, drawing,
                host -> new HistoryPanel(historyViewModel, host, historyPresenter::undoDelete),
                pdfPanel, historyPresenter::delete,
                () -> dialogs.get().save(), () -> dialogs.get().saveAs(),
                () -> dialogs.get().open());
        dialogs.set(new CanvasWorkspaceDialogs(canvases, drawing, board));
        FloatingToolbar toolbar = new FloatingToolbar(drawing, board, pdfPanel);
        board.addFloatingToolbar(toolbar.component());
        pdfPanel.setFullscreenAction(() -> board.setPdfFullscreen(!board.isPdfFullscreen()));
        pdfStudio.onOpen(() -> board.setPdfOpen(true));
        dialogs.get().restoreLatestAtStartup();
        board.setSaveStateSupplier(dialogs.get()::saveMenuState);
        drawing.addInkChangeListener(board::scheduleSaveStateRefresh);
        drawing.addFormulaChangeListener(id -> board.scheduleSaveStateRefresh());
        drawing.addViewListener(board::scheduleSaveStateRefresh);

        JFrame frame = new JFrame("Math Lab");
        JRootPane root = frame.getRootPane();
        if (BoardTheme.MAC) {  // before the window is shown
            root.putClientProperty("apple.awt.fullWindowContent", true);
            root.putClientProperty("apple.awt.transparentTitleBar", true);
            root.putClientProperty("apple.awt.windowTitleVisible", false);
        }
        int shortcut = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        // Canvas undo/redo only. The PDF workspace keeps its own Undo/Redo, and a text field
        // being typed in must never silently undo canvas ink instead.
        java.util.function.BooleanSupplier focusOutsideCanvas = () -> {
            java.awt.Component focused = java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            return focused instanceof javax.swing.text.JTextComponent
                    || (focused != null && SwingUtilities.isDescendingFrom(focused, pdfPanel));
        };
        java.awt.event.ActionListener canvasUndo = e -> { if (!focusOutsideCanvas.getAsBoolean()) drawing.undo(); };
        java.awt.event.ActionListener canvasRedo = e -> { if (!focusOutsideCanvas.getAsBoolean()) drawing.redo(); };
        root.registerKeyboardAction(canvasUndo, KeyStroke.getKeyStroke(KeyEvent.VK_Z, shortcut),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        root.registerKeyboardAction(canvasRedo, KeyStroke.getKeyStroke(KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        // The platform redo shortcut (Cmd+Shift+Z on macOS, Ctrl+Shift+Z elsewhere).
        root.registerKeyboardAction(canvasRedo,
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, shortcut | java.awt.event.InputEvent.SHIFT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        root.registerKeyboardAction(e -> {
            dialogs.get().save();
            board.refreshSaveState();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcut), JComponent.WHEN_IN_FOCUSED_WINDOW);
        root.registerKeyboardAction(e -> {
            dialogs.get().saveAs();
            board.refreshSaveState();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcut | java.awt.event.InputEvent.SHIFT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        root.registerKeyboardAction(e -> board.setPdfFullscreen(false),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        frame.setContentPane(board);
        // Closing asks about unsaved ink first. EXIT_ON_CLOSE quit before windowClosed ran, so
        // unsaved work was lost and the PDF editor and model were never closed.
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                if (!dialogs.get().confirmClose()) return;
                frame.dispose();
                // Queued after windowClosed, so every close listener runs before the JVM exits.
                SwingUtilities.invokeLater(() -> System.exit(0));
            }
        });
        if (!java.awt.GraphicsEnvironment.isHeadless() && java.awt.Desktop.isDesktopSupported()
                && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.APP_QUIT_HANDLER)) {
            // Cmd+Q would otherwise exit immediately; route it through the same unsaved-changes check.
            java.awt.Desktop.getDesktop().setQuitHandler((event, response) -> {
                response.cancelQuit();
                SwingUtilities.invokeLater(() -> frame.dispatchEvent(
                        new java.awt.event.WindowEvent(frame, java.awt.event.WindowEvent.WINDOW_CLOSING)));
            });
        }
        frame.setSize(1280, 790);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowOpened(java.awt.event.WindowEvent e) { toolbar.relayout(); }
        });
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) { pdfPanel.close(); }
        });
        frame.setLocationRelativeTo(null);
        return frame;
    }
}
