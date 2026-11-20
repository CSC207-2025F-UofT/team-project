package org.accessgraph.use_case.reroute;

import org.accessgraph.entity.AccessibilityProfile;
import org.accessgraph.entity.Connection;
import org.accessgraph.entity.Disruption;
import org.accessgraph.entity.Route;
import org.accessgraph.entity.Station;
import org.accessgraph.entity.TransitNetwork;
import org.accessgraph.use_case.plan_route.ProfileDataAccessInterface;
import org.accessgraph.use_case.plan_route.RouteFinder;
import org.accessgraph.use_case.plan_route.TransitNetworkDataAccessInterface;
import org.accessgraph.use_case.reroute.RerouteOutputData.Outcome;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simulates and restores disruptions on single connections. After each change it recalculates the
 * passenger's journey and reports whether the previous route is still valid, what replaces it, or that
 * no alternative exists.
 */
public final class RerouteInteractor implements RerouteInputBoundary {
    private final TransitNetworkDataAccessInterface networkAccess;
    private final ProfileDataAccessInterface profileAccess;
    private final DisruptionDataAccessInterface disruptionAccess;
    private final RouteFinder routeFinder;
    private final RerouteOutputBoundary presenter;

    /**
     * @param networkAccess supplies the full network, before any disruption is applied
     * @param profileAccess supplies the passenger's current accessibility profile
     * @param disruptionAccess stores the active disruptions
     * @param routeFinder searches the network that remains once the disruptions are applied
     * @param presenter receives the result
     */
    public RerouteInteractor(TransitNetworkDataAccessInterface networkAccess,
                             ProfileDataAccessInterface profileAccess,
                             DisruptionDataAccessInterface disruptionAccess,
                             RouteFinder routeFinder,
                             RerouteOutputBoundary presenter) {
        this.networkAccess = networkAccess;
        this.profileAccess = profileAccess;
        this.disruptionAccess = disruptionAccess;
        this.routeFinder = routeFinder;
        this.presenter = presenter;
    }

    @Override
    public void simulate(RerouteInputData inputData) {
        TransitNetwork network = networkAccess.getNetwork();
        if (!isSupported(network, inputData)) {
            presenter.prepareFailView("That connection is not part of the supported network.");
            return;
        }
        if (inputData.getType() == null) {
            presenter.prepareFailView("Choose the type of disruption to simulate.");
            return;
        }
        Disruption active = findActive(inputData);
        if (active != null) {
            DisruptedConnection connection = describe(network, active);
            presenter.prepareFailView(connection.getLine() + " between " + connection.getStationName() + " and "
                    + connection.getOtherStationName() + " is already disrupted.");
            return;
        }

        Disruption disruption = new Disruption(inputData.getType(), inputData.getLine(),
                inputData.getStationId(), inputData.getOtherStationId());
        Optional<Route> previous = findRoute(network, inputData);
        disruptionAccess.add(disruption);
        Optional<Route> current = findRoute(network, inputData);

        Outcome outcome;
        if (!hasJourney(network, inputData)) {
            outcome = Outcome.NO_JOURNEY;
        } else if (previous.isEmpty()) {
            outcome = Outcome.NO_ROUTE;
        } else if (!disruption.affects(previous.get())) {
            outcome = Outcome.UNCHANGED;
        } else {
            outcome = current.isPresent() ? Outcome.REROUTED : Outcome.NO_ALTERNATIVE;
        }
        presenter.prepareDisruptedView(toOutput(network, disruption, outcome, current));
    }

    @Override
    public void restore(RerouteInputData inputData) {
        TransitNetwork network = networkAccess.getNetwork();
        Disruption disruption = findActive(inputData);
        if (disruption == null) {
            presenter.prepareFailView("That connection is not disrupted.");
            return;
        }

        Optional<Route> previous = findRoute(network, inputData);
        disruptionAccess.remove(disruption);
        Optional<Route> current = findRoute(network, inputData);

        Outcome outcome;
        if (!hasJourney(network, inputData)) {
            outcome = Outcome.NO_JOURNEY;
        } else if (current.isEmpty()) {
            outcome = Outcome.NO_ROUTE;
        } else if (previous.isPresent() && !disruption.affects(current.get())) {
            outcome = Outcome.UNCHANGED;
        } else {
            outcome = Outcome.REROUTED;
        }
        presenter.prepareRestoredView(toOutput(network, disruption, outcome, current));
    }

    /** Whether the full network has a ride on the chosen line directly between the two chosen stations. */
    private static boolean isSupported(TransitNetwork network, RerouteInputData inputData) {
        String line = inputData.getLine();
        String stationId = inputData.getStationId();
        String otherStationId = inputData.getOtherStationId();
        if (line == null || stationId == null || otherStationId == null) {
            return false;
        }
        return connects(network, line, stationId, otherStationId)
                || connects(network, line, otherStationId, stationId);
    }

    private static boolean connects(TransitNetwork network, String line, String fromId, String toId) {
        for (Connection connection : network.connectionsFrom(fromId)) {
            if (connection.getLine().equals(line) && connection.getToId().equals(toId)) {
                return true;
            }
        }
        return false;
    }

    /** Returns the active disruption on the chosen connection, or null when it is not disrupted. */
    private Disruption findActive(RerouteInputData inputData) {
        for (Disruption disruption : disruptionAccess.getActiveDisruptions()) {
            if (disruption.isBetween(inputData.getLine(), inputData.getStationId(),
                    inputData.getOtherStationId())) {
                return disruption;
            }
        }
        return null;
    }

    /** Whether the passenger has searched for a journey between two different supported stations. */
    private static boolean hasJourney(TransitNetwork network, RerouteInputData inputData) {
        String originId = inputData.getOriginId();
        String destinationId = inputData.getDestinationId();
        return originId != null && destinationId != null && !originId.equals(destinationId)
                && network.hasStation(originId) && network.hasStation(destinationId);
    }

    /**
     * Returns the journey's route under the disruptions that are active right now. It is empty when there
     * is no journey, when the profile cannot use the origin or destination, or when no route is feasible.
     */
    private Optional<Route> findRoute(TransitNetwork network, RerouteInputData inputData) {
        if (!hasJourney(network, inputData)) {
            return Optional.empty();
        }
        AccessibilityProfile profile = profileAccess.getProfile();
        if (!profile.canUse(network.getStation(inputData.getOriginId()))
                || !profile.canUse(network.getStation(inputData.getDestinationId()))) {
            return Optional.empty();
        }
        TransitNetwork available = network.without(disruptionAccess.getActiveDisruptions());
        return routeFinder.findRoute(available, inputData.getOriginId(), inputData.getDestinationId(), profile);
    }

    private RerouteOutputData toOutput(TransitNetwork network, Disruption changed, Outcome outcome,
                                       Optional<Route> route) {
        List<DisruptedConnection> active = new ArrayList<>();
        for (Disruption disruption : disruptionAccess.getActiveDisruptions()) {
            active.add(describe(network, disruption));
        }
        List<String> stationNames = new ArrayList<>();
        if (route.isPresent()) {
            for (Station station : route.get().getStations()) {
                stationNames.add(station.getName());
            }
        }
        return new RerouteOutputData(describe(network, changed), active, outcome, stationNames,
                route.map(Route::getTotalMinutes).orElse(0), route.map(Route::getTransfers).orElse(0));
    }

    private static DisruptedConnection describe(TransitNetwork network, Disruption disruption) {
        return new DisruptedConnection(disruption.getType().getLabel(), disruption.getLine(),
                disruption.getStationId(), network.getStation(disruption.getStationId()).getName(),
                disruption.getOtherStationId(), network.getStation(disruption.getOtherStationId()).getName());
    }
}
