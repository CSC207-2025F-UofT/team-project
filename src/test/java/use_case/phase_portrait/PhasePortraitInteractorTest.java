package use_case.phase_portrait;

import data_access.InMemoryPhaseSessionDataAccessObject;
import entity.phase.DifferentialEquationParser;
import entity.phase.Equilibrium;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhasePortraitInteractorTest {
    private final RecordingPhasePresenter presenter = new RecordingPhasePresenter();
    private final InMemoryPhaseSessionDataAccessObject session = new InMemoryPhaseSessionDataAccessObject();
    private final PhasePortraitInteractor interactor =
            new PhasePortraitInteractor(new DifferentialEquationParser(), session, presenter);

    @Test
    void drawsASystemAndKeepsItForExploring() {
        interactor.execute(new PhasePortraitInputData("x'=y\\\\y'=-x"));
        assertNull(presenter.error);
        PhasePortraitOutputData d = presenter.last();
        assertTrue(presenter.fresh.get(0));
        assertFalse(d.isSlopeField());
        assertEquals("x", d.getHorizontalName());
        assertEquals(-10, d.getXMin(), 1e-12);
        assertEquals(-6.6, d.getYMin(), 1e-12);    // auto: about the card's shape
        assertEquals(4, d.getArrows().length);
        assertEquals(1, d.getEquilibria().size());
        assertEquals(Equilibrium.Type.CENTER, d.getEquilibria().get(0).getType());
        assertTrue(d.getSolutions().isEmpty());
        assertNotNull(session.current());
    }

    @Test
    void slopeFieldUsesTheTypedYRange() {
        interactor.execute(new PhasePortraitInputData("y'=y(1-y)", -3, 3, -1, 2));
        PhasePortraitOutputData d = presenter.last();
        assertTrue(d.isSlopeField());
        assertEquals(-1, d.getYMin(), 1e-12);
        assertEquals(2, d.getEquilibria().size());
        assertTrue(d.getEquilibria().get(0).isLine());
    }

    @Test
    void failsWithAReason() {
        interactor.execute(new PhasePortraitInputData("y''=x"));
        assertTrue(presenter.error.contains("autonomous"), presenter.error);
        interactor.execute(new PhasePortraitInputData("  "));
        assertEquals("No differential equation to draw.", presenter.error);
        interactor.execute(new PhasePortraitInputData("y'=x", 3, 1, Double.NaN, Double.NaN));
        assertTrue(presenter.error.contains("x range"));
        interactor.execute(new PhasePortraitInputData("y'=x", 1, 3, 5, 5));
        assertTrue(presenter.error.contains("y range"));
        interactor.execute(new PhasePortraitInputData("y'=\\sqrt{-1-y^2}"));
        assertTrue(presenter.error.contains("undefined everywhere"), presenter.error);
        assertTrue(presenter.views.isEmpty());
        assertNull(session.current());
    }
}
