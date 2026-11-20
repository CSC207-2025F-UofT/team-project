package org.accessgraph.view;

import org.accessgraph.interface_adapter.BrowserGateway;
import org.accessgraph.interface_adapter.TileGateway;
import org.accessgraph.use_case.sign_in.SignIn;
import org.accessgraph.use_case.station_map.LocationGateway;
import org.accessgraph.use_case.station_map.MapSession;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.time.Clock;

/** Navigation for the desktop demo. A valid sign-in opens a new map session. */
public final class AccessGraphPanel extends JPanel implements AutoCloseable {
    private static final String LOGIN = "login";
    private static final String MAP = "map";
    private final CardLayout cards = new CardLayout();
    private final LoginPanel login;
    private final MapPanel map;

    public AccessGraphPanel(SignIn signIn, String demoUsername, String demoPassword, MapSession session,
                            TileGateway tiles, LocationGateway locations, BrowserGateway browser, Clock clock) {
        setLayout(cards);
        map = new MapPanel(session, tiles, locations, browser, clock, this::signOut);
        login = new LoginPanel(demoUsername, demoPassword, (name, password) -> {
            if (!signIn.authenticate(name, password)) { return false; }
            cards.show(this, MAP);
            map.activate();
            return true;
        });
        add(login, LOGIN);
        add(map, MAP);
    }

    private void signOut() {
        map.deactivate();
        cards.show(this, LOGIN);
        SwingUtilities.invokeLater(login::focusUsername);
    }

    void focusSignIn() { login.focusUsername(); }
    @Override public void close() { map.deactivate(); }
}
