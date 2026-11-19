package use_case.explore_phase;

import data_access.InMemoryPhaseSessionDataAccessObject;
import entity.phase.DifferentialEquationParser;
import entity.phase.Equilibrium;
import org.junit.jupiter.api.Test;
import use_case.phase_portrait.PhasePortraitInputData;
import use_case.phase_portrait.PhasePortraitInteractor;
import use_case.phase_portrait.PhaseTraceOutputData;
import use_case.phase_portrait.RecordingPhasePresenter;

import static org.junit.jupiter.api.Assertions.*;

class ExplorePhaseInteractorTest {
    private final RecordingPhasePresenter presenter = new RecordingPhasePresenter();
    private final InMemoryPhaseSessionDataAccessObject session = new InMemoryPhaseSessionDataAccessObject();
    private final ExplorePhaseInteractor explore = new ExplorePhaseInteractor(session, presenter);

    private void draw(String latex) {
        new PhasePortraitInteractor(new DifferentialEquationParser(), session, presenter)
                .execute(new PhasePortraitInputData(latex, -5, 5, -4, 4));
    }

    @Test
    void nothingUpNothingHappens() {
        explore.pan(1, 1);
        explore.zoom(0, 0, 2, 2);
        explore.reset();
        explore.trace(0, 0, 1, 1);
        explore.toggleSolution(0, 0, 1, 1);
        assertTrue(presenter.views.isEmpty());
        assertTrue(presenter.traces.isEmpty());
    }

    @Test
    void panZoomAreNotFresh() {
        draw("x'=y\\\\y'=-x");
        explore.pan(2, 0);
        assertFalse(presenter.fresh.get(1));
        assertEquals(-3, presenter.last().getXMin(), 1e-12);
        explore.zoom(0, 0, 0.5, 0.5);
        assertEquals(-1.5, presenter.last().getXMin(), 1e-12);
        explore.zoom(0, 0, 1, 1);
        assertEquals(3, presenter.views.size());   // no change, no publish
    }

    @Test
    void pinFollowsPansAndResetClearsIt() {
        draw("x'=y\\\\y'=-x");
        explore.toggleSolution(2, 0, 0.05, 0.05);
        assertEquals(1, presenter.last().getSolutions().size());
        explore.pan(1, 1);
        assertEquals(1, presenter.last().getSolutions().size());
        explore.reset();
        assertTrue(presenter.last().getSolutions().isEmpty());
        assertEquals(-5, presenter.last().getXMin(), 1e-12);
    }

    @Test
    void traceSnapsAndEnds() {
        draw("x'=x\\\\y'=-y");
        explore.trace(0.03, 0.02, 0.1, 0.1);
        PhaseTraceOutputData t = presenter.traces.get(0);
        assertEquals(0, t.getH(), 1e-12);
        assertEquals(Equilibrium.Type.SADDLE, t.getEquilibrium().getType());
        explore.trace(1, 1, 0.01, 0.01);
        t = presenter.traces.get(1);
        assertEquals(1, t.getDh(), 1e-12);
        assertEquals(-1, t.getDv(), 1e-12);
        assertFalse(t.getPath().isEmpty());
        explore.endTrace();
        assertNull(presenter.traces.get(2));
    }
}
