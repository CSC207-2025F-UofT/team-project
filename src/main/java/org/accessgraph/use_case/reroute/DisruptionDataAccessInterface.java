package org.accessgraph.use_case.reroute;

import org.accessgraph.entity.Disruption;

import java.util.List;

/** Stores the disruptions that are currently active. Implemented in the data access layer. */
public interface DisruptionDataAccessInterface {
    List<Disruption> getActiveDisruptions();

    void add(Disruption disruption);

    void remove(Disruption disruption);
}
