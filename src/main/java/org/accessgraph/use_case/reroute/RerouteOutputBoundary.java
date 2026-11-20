package org.accessgraph.use_case.reroute;

/** Implemented by the presenter; the interactor reports its result through this interface. */
public interface RerouteOutputBoundary {
    /** A disruption was simulated and the journey was recalculated. */
    void prepareDisruptedView(RerouteOutputData outputData);

    /** A connection was restored and the journey was recalculated. */
    void prepareRestoredView(RerouteOutputData outputData);

    /** Nothing changed because the request could not be carried out. */
    void prepareFailView(String errorMessage);
}
