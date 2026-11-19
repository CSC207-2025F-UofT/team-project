package data_access;

import entity.phase.PhasePlot;
import use_case.phase_portrait.PhaseSessionDataAccessInterface;

// the phase portrait that's up, drawing runs off the ui thread so it's locked
public class InMemoryPhaseSessionDataAccessObject implements PhaseSessionDataAccessInterface {
    private PhasePlot current;

    @Override
    public synchronized void save(PhasePlot plot) {
        current = plot;
    }

    @Override
    public synchronized PhasePlot current() {
        return current;
    }
}
