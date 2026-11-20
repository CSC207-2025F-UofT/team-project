package org.accessgraph.use_case.plan_route;

import org.accessgraph.entity.AccessibilityProfile;
import org.accessgraph.entity.Connection;
import org.accessgraph.entity.Station;
import org.accessgraph.entity.TransitNetwork;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** The interactor is tested with fake data access and a fake presenter: no Swing, no files. */
class PlanRouteInteractorTest {
    private static final AccessibilityProfile STEP_FREE = new AccessibilityProfile(true, 2);

    private static final class FakePresenter implements PlanRouteOutputBoundary {
        private PlanRouteOutputData success;
        private String error;

        @Override
        public void prepareSuccessView(PlanRouteOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }

    private static Station station(String id, String name, Station.Accessibility accessibility) {
        return new Station(id, name, 43.6, -79.4, accessibility, URI.create("https://www.ttc.ca/subway-stations/x"),
                LocalDate.of(2026, 11, 9));
    }

    private static TransitNetwork network() {
        List<Station> stations = List.of(station("1", "Union", Station.Accessibility.CONFIRMED),
                station("2", "King", Station.Accessibility.LIMITED),
                station("3", "Queen", Station.Accessibility.CONFIRMED));
        return new TransitNetwork(stations, List.of(
                new Connection("1", "2", "Line 1", 2), new Connection("2", "1", "Line 1", 2),
                new Connection("2", "3", "Line 1", 2), new Connection("3", "2", "Line 1", 2)));
    }

    private static FakePresenter run(String from, String to) {
        FakePresenter presenter = new FakePresenter();
        TransitNetwork network = network();
        new PlanRouteInteractor(() -> network, () -> STEP_FREE, new DijkstraRouteFinder(), presenter)
                .execute(new PlanRouteInputData(from, to));
        return presenter;
    }

    @Test
    void successShowsStationsDurationAndTransfers() {
        FakePresenter presenter = run("1", "3");
        assertNull(presenter.error);
        assertEquals(List.of("Union", "King", "Queen"), presenter.success.getStationNames());
        assertEquals(4, presenter.success.getTotalMinutes());
        assertEquals(0, presenter.success.getTransfers());
    }

    @Test
    void sameStationIsRejected() {
        assertTrue(run("1", "1").error.contains("same"));
    }

    @Test
    void unknownStationIsRejected() {
        assertTrue(run("1", "999").error.contains("not supported"));
    }

    @Test
    void inaccessibleDestinationIsExplained() {
        assertTrue(run("1", "2").error.contains("King"));
    }
}
