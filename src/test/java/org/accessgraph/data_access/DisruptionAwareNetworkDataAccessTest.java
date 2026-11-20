package org.accessgraph.data_access;

import org.accessgraph.entity.AccessibilityProfile;
import org.accessgraph.entity.Connection;
import org.accessgraph.entity.Disruption;
import org.accessgraph.entity.Station;
import org.accessgraph.entity.TransitNetwork;
import org.accessgraph.use_case.plan_route.DijkstraRouteFinder;
import org.accessgraph.use_case.plan_route.PlanRouteInputData;
import org.accessgraph.use_case.plan_route.PlanRouteInteractor;
import org.accessgraph.use_case.plan_route.PlanRouteOutputBoundary;
import org.accessgraph.use_case.plan_route.PlanRouteOutputData;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Test network: A --L1-- B directly (2 minutes), or A --L2-- C --L2-- B the long way (10 minutes). */
class DisruptionAwareNetworkDataAccessTest {
    private final InMemoryDisruptionDataAccess disruptions = new InMemoryDisruptionDataAccess();
    private final TransitNetwork network = network();
    private final DisruptionAwareNetworkDataAccess networkAccess =
            new DisruptionAwareNetworkDataAccess(() -> network, disruptions);

    private static Station station(String id) {
        return new Station(id, id, 43.6, -79.4, Station.Accessibility.CONFIRMED,
                URI.create("https://www.ttc.ca/subway-stations/x"), LocalDate.of(2026, 11, 9));
    }

    private static TransitNetwork network() {
        List<Connection> connections = new ArrayList<>();
        for (String[] row : new String[][]{{"A", "B", "L1", "2"}, {"A", "C", "L2", "5"}, {"C", "B", "L2", "5"}}) {
            connections.add(new Connection(row[0], row[1], row[2], Integer.parseInt(row[3])));
            connections.add(new Connection(row[1], row[0], row[2], Integer.parseInt(row[3])));
        }
        return new TransitNetwork(List.of(station("A"), station("B"), station("C")), connections);
    }

    /** Plans A to B with the route-planning use case, reading the network through the decorator. */
    private List<String> plannedStations() {
        List<String> stations = new ArrayList<>();
        PlanRouteOutputBoundary presenter = new PlanRouteOutputBoundary() {
            @Override
            public void prepareSuccessView(PlanRouteOutputData outputData) {
                stations.addAll(outputData.getStationNames());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                stations.add(errorMessage);
            }
        };
        new PlanRouteInteractor(networkAccess, () -> new AccessibilityProfile(true, 2),
                new DijkstraRouteFinder(), presenter).execute(new PlanRouteInputData("A", "B"));
        return stations;
    }

    @Test
    void networkIsUnchangedWhileNothingIsDisrupted() {
        assertEquals(2, networkAccess.getNetwork().connectionsFrom("A").size());
        assertEquals(List.of("A", "B"), plannedStations());
    }

    @Test
    void routePlanningAvoidsADisruptedConnectionUntilItIsRestored() {
        Disruption outage = new Disruption(Disruption.Type.OUTAGE, "L1", "A", "B");
        disruptions.add(outage);

        assertEquals(1, networkAccess.getNetwork().connectionsFrom("A").size());
        assertEquals("L2", networkAccess.getNetwork().connectionsFrom("A").get(0).getLine());
        assertEquals(List.of("A", "C", "B"), plannedStations());

        disruptions.remove(outage);
        assertTrue(disruptions.getActiveDisruptions().isEmpty());
        assertEquals(List.of("A", "B"), plannedStations());
    }
}
