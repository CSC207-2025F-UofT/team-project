package org.accessgraph.app;

import org.accessgraph.data_access.BrowserLocationGateway;
import org.accessgraph.data_access.BundledStationRepository;
import org.accessgraph.data_access.DemoAccount;
import org.accessgraph.data_access.DesktopBrowser;
import org.accessgraph.data_access.OpenStreetMapGateway;
import org.accessgraph.entity.Station;
import org.accessgraph.use_case.sign_in.SignIn;
import org.accessgraph.use_case.station_map.MapSession;
import org.accessgraph.view.AccessGraphPanel;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/** The composition root connects the use cases and their desktop adapters. */
public final class AppFactory {
    private AppFactory() { }

    public static AccessGraphPanel createPanel() {
        HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build();
        return new AccessGraphPanel(new SignIn(new DemoAccount()), DemoAccount.USERNAME, DemoAccount.PASSWORD,
                new MapSession(loadStations()), new OpenStreetMapGateway(http), new BrowserLocationGateway(),
                new DesktopBrowser(), Clock.systemUTC());
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
