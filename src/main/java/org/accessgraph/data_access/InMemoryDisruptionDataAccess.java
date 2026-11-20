package org.accessgraph.data_access;

import org.accessgraph.entity.Disruption;
import org.accessgraph.use_case.reroute.DisruptionDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/** Keeps simulated disruptions in memory. They last until the application closes. */
public final class InMemoryDisruptionDataAccess implements DisruptionDataAccessInterface {
    private final List<Disruption> active = new ArrayList<>();

    @Override
    public List<Disruption> getActiveDisruptions() {
        return List.copyOf(active);
    }

    @Override
    public void add(Disruption disruption) {
        active.add(disruption);
    }

    @Override
    public void remove(Disruption disruption) {
        active.remove(disruption);
    }
}
