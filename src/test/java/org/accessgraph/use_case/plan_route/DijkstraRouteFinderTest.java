package org.accessgraph.use_case.plan_route;

import org.accessgraph.entity.AccessibilityProfile;
import org.accessgraph.entity.Connection;
import org.accessgraph.entity.Route;
import org.accessgraph.entity.Station;
import org.accessgraph.entity.TransitNetwork;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test network:  A --L1-- X --L1-- B     (X is a limited station)
 *                A --L2-- Y --L3-- B     (Y is confirmed; changing lines there is allowed)
 * The L1 path is faster but requires no transfer; a direct path through X is fine even for step-free
 * passengers because they stay on the train.
 */
class DijkstraRouteFinderTest {
    private final RouteFinder finder = new DijkstraRouteFinder();

    private static Station station(String id, Station.Accessibility accessibility) {
        return new Station(id, id, 43.6, -79.4, accessibility, URI.create("https://www.ttc.ca/subway-stations/x"),
                LocalDate.of(2026, 11, 9));
    }

    private static void both(List<Connection> list, String a, String b, String line, int minutes) {
        list.add(new Connection(a, b, line, minutes));
        list.add(new Connection(b, a, line, minutes));
    }

    private static TransitNetwork network(Station.Accessibility xAccess, boolean includeDirectLine) {
        List<Station> stations = List.of(station("A", Station.Accessibility.CONFIRMED), station("X", xAccess),
                station("B", Station.Accessibility.CONFIRMED), station("Y", Station.Accessibility.CONFIRMED));
        List<Connection> connections = new ArrayList<>();
        if (includeDirectLine) {
            both(connections, "A", "X", "L1", 2);
            both(connections, "X", "B", "L1", 2);
        }
        both(connections, "A", "Y", "L2", 2);
        both(connections, "Y", "B", "L3", 2);
        return new TransitNetwork(stations, connections);
    }

    @Test
    void findsFastestRouteAndRidesThroughLimitedStation() {
        Optional<Route> route = finder.findRoute(network(Station.Accessibility.LIMITED, true), "A", "B",
                new AccessibilityProfile(true, 2));
        assertTrue(route.isPresent());
        assertEquals(4, route.get().getTotalMinutes());
        assertEquals(0, route.get().getTransfers());
        assertEquals(List.of("L1", "L1"), route.get().getLegLines());
    }

    @Test
    void addsTransferPenaltyWhenChangingLines() {
        Optional<Route> route = finder.findRoute(network(Station.Accessibility.CONFIRMED, false), "A", "B",
                new AccessibilityProfile(true, 2));
        assertTrue(route.isPresent());
        assertEquals(1, route.get().getTransfers());
        assertEquals(4 + DijkstraRouteFinder.TRANSFER_MINUTES, route.get().getTotalMinutes());
    }

    @Test
    void respectsMaximumTransfers() {
        Optional<Route> route = finder.findRoute(network(Station.Accessibility.CONFIRMED, false), "A", "B",
                new AccessibilityProfile(false, 0));
        assertTrue(route.isEmpty());
    }

    @Test
    void stepFreePassengerCannotChangeLinesAtLimitedStation() {
        List<Station> stations = List.of(station("A", Station.Accessibility.CONFIRMED),
                station("X", Station.Accessibility.LIMITED), station("B", Station.Accessibility.CONFIRMED));
        List<Connection> connections = new ArrayList<>();
        both(connections, "A", "X", "L1", 2);
        both(connections, "X", "B", "L2", 2);
        TransitNetwork network = new TransitNetwork(stations, connections);

        assertTrue(finder.findRoute(network, "A", "B", new AccessibilityProfile(true, 2)).isEmpty());
        assertTrue(finder.findRoute(network, "A", "B", new AccessibilityProfile(false, 2)).isPresent());
    }
}
