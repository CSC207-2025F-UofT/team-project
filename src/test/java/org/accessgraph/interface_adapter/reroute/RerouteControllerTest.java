package org.accessgraph.interface_adapter.reroute;

import org.accessgraph.entity.Disruption;
import org.accessgraph.use_case.plan_route.PlanRouteInputData;
import org.accessgraph.use_case.reroute.RerouteInputBoundary;
import org.accessgraph.use_case.reroute.RerouteInputData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RerouteControllerTest {
    /** Everything that happened, in order: route searches and reroute requests. */
    private final List<String> calls = new ArrayList<>();
    private final List<PlanRouteInputData> searches = new ArrayList<>();
    private final JourneyRecorder journeys = new JourneyRecorder(inputData -> {
        calls.add("search");
        searches.add(inputData);
    });
    private RerouteInputData simulated;
    private RerouteInputData restored;
    private final RerouteController controller = new RerouteController(new RerouteInputBoundary() {
        @Override
        public void simulate(RerouteInputData inputData) {
            calls.add("simulate");
            simulated = inputData;
        }

        @Override
        public void restore(RerouteInputData inputData) {
            calls.add("restore");
            restored = inputData;
        }
    }, journeys);

    @Test
    void simulateSendsTheChosenConnectionWithTheLastSearchedJourney() {
        PlanRouteInputData journey = new PlanRouteInputData("origin", "destination");
        journeys.execute(journey);

        controller.simulate("Line 1", "1", "2", "CLOSURE");

        assertEquals("Line 1", simulated.getLine());
        assertEquals("1", simulated.getStationId());
        assertEquals("2", simulated.getOtherStationId());
        assertEquals(Disruption.Type.CLOSURE, simulated.getType());
        assertEquals("origin", simulated.getOriginId());
        assertEquals("destination", simulated.getDestinationId());
        assertEquals(List.of("search", "simulate", "search"), calls, "The planner is refreshed after the change.");
        assertSame(journey, searches.get(1));
    }

    @Test
    void restoreSendsTheConnectionAndRefreshesThePlanner() {
        journeys.execute(new PlanRouteInputData("origin", "destination"));

        controller.restore("Line 2", "3", "4");

        assertEquals("Line 2", restored.getLine());
        assertEquals("3", restored.getStationId());
        assertEquals("4", restored.getOtherStationId());
        assertNull(restored.getType());
        assertEquals("origin", restored.getOriginId());
        assertEquals(List.of("search", "restore", "search"), calls);
    }

    @Test
    void nothingIsSearchedBeforeThePassengerPlansAJourney() {
        controller.simulate("Line 1", "1", "2", null);

        assertNull(simulated.getType());
        assertNull(simulated.getOriginId());
        assertNull(simulated.getDestinationId());
        assertEquals(List.of("simulate"), calls);
        assertTrue(searches.isEmpty());
    }
}
