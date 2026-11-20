package org.accessgraph.use_case.reroute;

/** Called by the controller to change the simulated conditions and recalculate the passenger's journey. */
public interface RerouteInputBoundary {
    /** Makes a supported connection unavailable, then recalculates the journey. */
    void simulate(RerouteInputData inputData);

    /** Makes a disrupted connection available again, then recalculates the journey. */
    void restore(RerouteInputData inputData);
}
