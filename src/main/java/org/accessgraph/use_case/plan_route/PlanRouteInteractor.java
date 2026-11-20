package org.accessgraph.use_case.plan_route;

import org.accessgraph.entity.AccessibilityProfile;
import org.accessgraph.entity.Route;
import org.accessgraph.entity.Station;
import org.accessgraph.entity.TransitNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Finds a route between two stations that satisfies the passenger's accessibility profile. */
public final class PlanRouteInteractor implements PlanRouteInputBoundary {
    private final TransitNetworkDataAccessInterface networkAccess;
    private final ProfileDataAccessInterface profileAccess;
    private final RouteFinder routeFinder;
    private final PlanRouteOutputBoundary presenter;

    public PlanRouteInteractor(TransitNetworkDataAccessInterface networkAccess,
                               ProfileDataAccessInterface profileAccess,
                               RouteFinder routeFinder,
                               PlanRouteOutputBoundary presenter) {
        this.networkAccess = networkAccess;
        this.profileAccess = profileAccess;
        this.routeFinder = routeFinder;
        this.presenter = presenter;
    }

    @Override
    public void execute(PlanRouteInputData inputData) {
        String originId = inputData.getOriginId();
        String destinationId = inputData.getDestinationId();
        TransitNetwork network = networkAccess.getNetwork();
        AccessibilityProfile profile = profileAccess.getProfile();

        if (originId == null || destinationId == null) {
            presenter.prepareFailView("Choose both a starting station and a destination.");
            return;
        }
        if (!network.hasStation(originId) || !network.hasStation(destinationId)) {
            presenter.prepareFailView("That station is not supported yet.");
            return;
        }
        if (originId.equals(destinationId)) {
            presenter.prepareFailView("The starting station and destination are the same.");
            return;
        }
        Station origin = network.getStation(originId);
        Station destination = network.getStation(destinationId);
        for (Station station : List.of(origin, destination)) {
            if (!profile.canUse(station)) {
                presenter.prepareFailView(station.getName() + " is not confirmed as accessible by the TTC, "
                        + "so it cannot be used with a step-free profile.");
                return;
            }
        }

        Optional<Route> route = routeFinder.findRoute(network, originId, destinationId, profile);
        if (route.isEmpty()) {
            presenter.prepareFailView("No route between " + origin.getName() + " and " + destination.getName()
                    + " meets your accessibility profile.");
            return;
        }
        presenter.prepareSuccessView(toOutput(route.get(), profile));
    }

    private PlanRouteOutputData toOutput(Route route, AccessibilityProfile profile) {
        List<String> names = new ArrayList<>();
        for (Station station : route.getStations()) {
            names.add(station.getName());
        }
        return new PlanRouteOutputData(names, route.getLegLines(), route.getTotalMinutes(),
                route.getTransfers(), profile.isStepFreeRequired());
    }
}
