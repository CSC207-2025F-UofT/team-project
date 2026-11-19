package view.canvas;

import interface_adapter.phase.PhaseController;
import interface_adapter.phase.PhaseExploreController;
import interface_adapter.phase.PhaseState;
import interface_adapter.phase.PhaseViewModel;
import view.FormulaCapture;
import view.graph.PhaseCard;
import view.graph.PhaseSnapshot;

import javax.swing.JComponent;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

// Phase Portrait from the popup: its own use case + cards, side by side with Graph's on the canvas
final class PopupPhasePortrait extends PopupCardTool<PhaseSession> {


    // one shared session: a single live card, a new portrait replaces it
    PopupPhasePortrait(SharedCanvas canvas, PhaseController controller, PhaseExploreController explore,
                       PhaseViewModel viewModel, Notices notices, JComponent area,
                       BiConsumer<FormulaCapture, String> saved) {
        this(canvas, shared(new PhaseSession(controller, explore, viewModel)), notices, area, saved);
    }

    // a fresh session per portrait: several on the canvas at once
    PopupPhasePortrait(SharedCanvas canvas, Supplier<PhaseSession> kits, Notices notices, JComponent area,
                       BiConsumer<FormulaCapture, String> saved) {
        super(canvas, kits, notices, area, saved, "popup-phase",
                "Can't draw the phase portrait", "Drawing the phase portrait of", "Phase portrait of");
    }

    private static Supplier<PhaseSession> shared(PhaseSession kit) {
        return () -> kit;
    }

    void draw(String latex, Rectangle formulaWorld, FormulaCapture capture) {
        run(latex, formulaWorld, capture);
    }

    @Override
    Live newLive(PhaseSession kit) {
        return new PhaseLive(kit);
    }

    private final class PhaseLive extends Live {
        private final PhaseViewModel viewModel;
        private volatile String asked;   // latex sent, the state shows a retitled one for systems

        PhaseLive(PhaseSession kit) {
            super(kit, new PhaseCard(kit.viewModel(), kit.explore()));
            this.viewModel = kit.viewModel();
            viewModel.addPropertyChangeListener(e -> {
                if (PhaseViewModel.STATE_PROPERTY.equals(e.getPropertyName())) {
                    stateChanged();
                } else if (PhaseViewModel.VIEW_PROPERTY.equals(e.getPropertyName())) {
                    viewChanged();
                }
            });
        }

        @Override
        void execute(String latex, double[] xRange, double[] yRange) {
            asked = latex;
            kit.controller().execute(latex, xRange, yRange);
        }

        @Override
        String sourceLatex() {
            return asked;
        }

        @Override
        String error() {
            return viewModel.getState().getError();
        }

        @Override
        boolean hasContent() {
            return viewModel.getState().hasPortrait();
        }

        @Override
        String latex() {
            return viewModel.getState().getLatex();
        }

        @Override
        double[] window() {
            PhaseState s = viewModel.getState();
            return new double[]{s.getXMin(), s.getXMax(), s.getYMin(), s.getYMax()};
        }

        @Override
        boolean followsY() {
            return true;
        }

        @Override
        BufferedImage firstPicture() {
            return PhaseSnapshot.render(viewModel.getState(), SNAPSHOT_WIDTH, SNAPSHOT_HEIGHT);
        }
    }
}
