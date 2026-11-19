package org.accessgraph.app;

import org.accessgraph.interface_adapter.PrototypeController;
import org.accessgraph.interface_adapter.PrototypeState;
import org.accessgraph.view.AccessGraphPanel;
import org.accessgraph.view.StationMapPanel;
import org.accessgraph.data_access.BundledStationRepository;
import org.accessgraph.data_access.BrowserLocationGateway;
import org.accessgraph.data_access.DesktopBrowser;
import org.accessgraph.data_access.OpenStreetMapGateway;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;

/** Composition root. Future gateways and interactors will be wired here. */
public final class AppFactory {
    private AppFactory() { }

    public static AccessGraphPanel createPanel() {
        PrototypeState state = new PrototypeState();
        PrototypeController controller = new PrototypeController(state);
        JComponent stationMap;
        try {
            stationMap = new StationMapPanel(new BundledStationRepository().load(),
                    new OpenStreetMapGateway(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build()),
                    new DesktopBrowser(), new BrowserLocationGateway());
        } catch (IOException exception) {
            JPanel error = new JPanel(new BorderLayout());
            error.add(new JLabel("Station catalogue unavailable. Rebuild the application with its bundled resources."),
                    BorderLayout.NORTH);
            stationMap = error;
        }
        return new AccessGraphPanel(state, controller, stationMap);
    }
}
