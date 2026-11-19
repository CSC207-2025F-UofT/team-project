package use_case.phase_portrait;

import entity.phase.PhasePlot;

// the portrait that's up right now, shared by drawing + exploring
public interface PhaseSessionDataAccessInterface {
    void save(PhasePlot plot);

    PhasePlot current();
}
