package org.accessgraph.data_access;

import org.accessgraph.entity.TransitNetwork;
import org.accessgraph.use_case.plan_route.TransitNetworkDataAccessInterface;
import org.accessgraph.use_case.reroute.DisruptionDataAccessInterface;

/**
 * Decorator that applies the active disruptions to another gateway's network. Any use case that reads
 * the network through it searches only the connections that are available right now.
 */
public final class DisruptionAwareNetworkDataAccess implements TransitNetworkDataAccessInterface {
    private final TransitNetworkDataAccessInterface networkAccess;
    private final DisruptionDataAccessInterface disruptionAccess;

    public DisruptionAwareNetworkDataAccess(TransitNetworkDataAccessInterface networkAccess,
                                            DisruptionDataAccessInterface disruptionAccess) {
        this.networkAccess = networkAccess;
        this.disruptionAccess = disruptionAccess;
    }

    @Override
    public TransitNetwork getNetwork() {
        return networkAccess.getNetwork().without(disruptionAccess.getActiveDisruptions());
    }
}
