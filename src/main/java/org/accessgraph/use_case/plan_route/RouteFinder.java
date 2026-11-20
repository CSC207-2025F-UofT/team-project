package org.accessgraph.use_case.plan_route;

import org.accessgraph.entity.AccessibilityProfile;
import org.accessgraph.entity.Route;
import org.accessgraph.entity.TransitNetwork;

import java.util.Optional;

/** Strategy for searching a network. Implementations can be swapped without changing the interactor. */
public interface RouteFinder {
    Optional<Route> findRoute(TransitNetwork network, String originId, String destinationId,
                              AccessibilityProfile profile);
}
