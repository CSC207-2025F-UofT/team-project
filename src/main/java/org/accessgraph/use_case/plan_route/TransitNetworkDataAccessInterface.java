package org.accessgraph.use_case.plan_route;

import org.accessgraph.entity.TransitNetwork;

/** Supplies the current transit network. Implemented in the data access layer. */
public interface TransitNetworkDataAccessInterface {
    TransitNetwork getNetwork();
}
