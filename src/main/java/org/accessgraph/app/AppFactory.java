package org.accessgraph.app;

import org.accessgraph.data_access.BrowserLocationGateway;
import org.accessgraph.data_access.BundledStationRepository;
import org.accessgraph.data_access.DemoAccount;
import org.accessgraph.data_access.DesktopBrowser;
import org.accessgraph.data_access.FileTransitNetworkDataAccess;
import org.accessgraph.data_access.InMemoryProfileDataAccess;
import org.accessgraph.entity.AccessibilityProfile;
import org.accessgraph.data_access.OpenStreetMapGateway;
import org.accessgraph.entity.Station;
import org.accessgraph.interface_adapter.plan_route.PlanRouteController;
import org.accessgraph.interface_adapter.plan_route.PlanRoutePresenter;
import org.accessgraph.interface_adapter.plan_route.PlanRouteViewModel;
import org.accessgraph.use_case.plan_route.DijkstraRouteFinder;
import org.accessgraph.use_case.plan_route.PlanRouteInteractor;
import org.accessgraph.use_case.sign_in.SignIn;
import org.accessgraph.use_case.station_map.MapSession;
import org.accessgraph.view.AccessGraphPanel;
import org.accessgraph.view.PlanRoutePanel;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** The composition root connects the use cases and their desktop adapters. */
public final class AppFactory {
    private AppFactory() { }

    public static AccessGraphPanel createPanel() {
        HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build();
        return new AccessGraphPanel(new SignIn(new DemoAccount()), DemoAccount.USERNAME, DemoAccount.PASSWORD,
                new MapSession(loadStations()), new OpenStreetMapGateway(http), new BrowserLocationGateway(),
                new DesktopBrowser(), Clock.systemUTC(), createRoutePlanner());
    }

    /** Builds the plan-route use case: view -> controller -> interactor -> presenter -> view model. */
    private static PlanRoutePanel createRoutePlanner() {
        try {
            FileTransitNetworkDataAccess networkAccess =
                    new FileTransitNetworkDataAccess(new BundledStationRepository().load());
            InMemoryProfileDataAccess profileAccess =
                    new InMemoryProfileDataAccess(new AccessibilityProfile(true, 2));
            PlanRouteViewModel viewModel = new PlanRouteViewModel();
            PlanRoutePresenter presenter = new PlanRoutePresenter(viewModel);
            PlanRouteInteractor interactor = new PlanRouteInteractor(networkAccess, profileAccess,
                    new DijkstraRouteFinder(), presenter);
            List<PlanRoutePanel.StationChoice> choices = new ArrayList<>();
            for (Station station : networkAccess.getNetwork().getConnectedStations()) {
                choices.add(new PlanRoutePanel.StationChoice(station.getId(), station.getName()));
            }
            return new PlanRoutePanel(choices, new PlanRouteController(interactor), viewModel);
        } catch (IOException exception) {
            System.err.println("The route planner could not load its network: " + exception.getMessage());
            return null;
        }
    }

    private static List<Station> loadStations() {
        try {
            return new BundledStationRepository().load().stream()
                    .filter(station -> station.getAccessibility() == Station.Accessibility.CONFIRMED)
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            System.err.println("The bundled TTC catalogue could not be read: " + exception.getMessage());
            return List.of();
        }
    }
}
