package interface_adapter.phase;

import entity.phase.Equilibrium;
import org.junit.jupiter.api.Test;
import use_case.phase_portrait.EquilibriumOutputData;
import use_case.phase_portrait.PhasePortraitOutputData;
import use_case.phase_portrait.PhaseTraceOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhasePresenterTest {
    private final PhaseViewModel viewModel = new PhaseViewModel();
    private final PhasePresenter presenter = new PhasePresenter(viewModel);
    private final List<String> events = new ArrayList<>();

    PhasePresenterTest() {
        viewModel.addPropertyChangeListener(e -> events.add(e.getPropertyName()));
    }

    private static PhasePortraitOutputData plane(String h, String v, List<EquilibriumOutputData> eq) {
        return new PhasePortraitOutputData("x'=y\\\\y'=-x", false, h, v, -5, 5, -4, 4,
                new double[4][0], eq, List.of());
    }

    @Test
    void freshPortraitBumpsTheRevisionAndPansDont() {
        presenter.preparePortraitView(plane("x", "y", List.of()), true);
        assertEquals(1, viewModel.getState().getRevision());
        assertEquals(PhaseState.Kind.PHASE_PLANE, viewModel.getState().getKind());
        assertFalse(viewModel.getState().getXTicks().isEmpty());
        presenter.preparePortraitView(plane("x", "y", List.of()), false);
        assertEquals(1, viewModel.getState().getRevision());
        assertEquals(List.of(PhaseViewModel.STATE_PROPERTY, PhaseViewModel.VIEW_PROPERTY), events);
    }

    @Test
    void namesEquilibria() {
        presenter.preparePortraitView(plane("x", "y", List.of(
                new EquilibriumOutputData(0, 0, Equilibrium.Type.SADDLE),
                new EquilibriumOutputData(1, 1, Equilibrium.Type.STABLE_SPIRAL))), true);
        List<PhaseMarker> m = viewModel.getState().getMarkers();
        assertEquals("Saddle point", m.get(0).getCaption());
        assertFalse(m.get(0).isStable());
        assertEquals("Stable spiral", m.get(1).getCaption());
        assertTrue(m.get(1).isStable());
    }

    @Test
    void readoutSaysThePointAndTheArrow() {
        presenter.preparePortraitView(plane("\\theta", "\\theta'", List.of()), true);
        assertEquals("θ", viewModel.getState().getHorizontalLabel());
        assertEquals("θ′", viewModel.getState().getVerticalLabel());
        presenter.prepareTraceView(new PhaseTraceOutputData(1.5, -2, -2, -0.5, false, null, List.of()));
        PhaseReadout r = viewModel.getState().getTrace();
        assertEquals("(1.5, -2)", r.getText());
        assertEquals("1.5", r.getHText());
        assertEquals(PhaseMarker.Role.SADDLE, PhasePresenter.role(Equilibrium.Type.SADDLE));
        assertEquals("θ′ = -2,  θ″ = -0.5", r.getDetail());
        assertEquals("Trajectory through", r.getCaption());
        presenter.prepareTraceView(null);
        assertNull(viewModel.getState().getTrace());
    }

    @Test
    void slopeFieldReadoutGivesTheSlopeAndNamesTheLine() {
        presenter.preparePortraitView(new PhasePortraitOutputData("y'=y(1-y)", true, "x", "y", -5, 5, -4, 4,
                new double[4][0], List.of(), List.of()), true);
        presenter.prepareTraceView(new PhaseTraceOutputData(2, 1, 1, 0, true,
                new EquilibriumOutputData(Double.NaN, 1, Equilibrium.Type.STABLE_LINE), List.of()));
        PhaseReadout r = viewModel.getState().getTrace();
        assertEquals("slope y′ = 0", r.getDetail());
        assertEquals("Stable equilibrium  y = 1", r.getCaption());
    }

    @Test
    void systemTitleGoesOnOneLine() {
        assertEquals("x'=y,\\quad y'=-x", PhasePresenter.title("\\begin{matrix}x'=y\\\\y'=-x\\end{matrix}"));
        assertEquals("x'=y,\\quad y'=-x",
                PhasePresenter.title("\\left\\{\\begin{array}{l}x'=y\\\\y'=-x\\end{array}\\right."));
        assertEquals("y'=x", PhasePresenter.title("y'=x"));
    }

    @Test
    void failKeepsThePortraitUp() {
        presenter.preparePortraitView(plane("x", "y", List.of()), true);
        presenter.prepareFailView("nope");
        assertEquals("nope", viewModel.getState().getError());
        assertTrue(viewModel.getState().hasPortrait());
        presenter.preparePortraitView(plane("x", "y", List.of()), true);
        assertNull(viewModel.getState().getError());
    }
}
