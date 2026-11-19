package org.accessgraph.view;

import org.accessgraph.interface_adapter.PrototypeController;
import org.accessgraph.interface_adapter.PrototypeState;
import org.accessgraph.interface_adapter.PrototypeState.Screen;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;

/** Root Swing view; can be built without a window for headless UI verification. */
public final class AccessGraphPanel extends JPanel implements AutoCloseable {
    private final JComponent stationMap;
    public AccessGraphPanel(PrototypeState state, PrototypeController controller, JComponent stationMap) {
        super(new BorderLayout());
        this.stationMap = stationMap;
        Ui.configure();
        setBackground(Ui.BACKGROUND);
        JPanel header = new JPanel(new BorderLayout(24, 0));
        header.setBackground(Ui.INK);
        header.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));
        JPanel brand = Ui.column();
        JLabel name = Ui.label("AccessGraph", 28, true);
        name.setForeground(Color.WHITE);
        Ui.add(brand, name, 5);
        JLabel subtitle = Ui.label("Accessible journeys on the TTC", 15, false);
        subtitle.setForeground(new Color(0xD8E8EC));
        Ui.add(brand, subtitle, 0);
        header.add(brand, BorderLayout.CENTER);
        JLabel badge = Ui.label("Interface prototype", 15, true);
        badge.setForeground(Color.WHITE);
        badge.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0x83BDBA)),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        header.add(badge, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.getAccessibleContext().setAccessibleName("AccessGraph screens");
        tabs.addTab("Plan journey", stationMap);
        tabs.addTab("Station pairs", new PlannerPanel(state, controller));
        tabs.addTab("Settings", new ProfilePanel(state, controller));
        tabs.addTab("Disruption simulation", new DisruptionsPanel(state, controller));
        tabs.addTab("Saved trips", new SavedTripsPanel(state, controller));
        for (int index = 0; index < tabs.getTabCount(); index++) {
            tabs.setMnemonicAt(index, '1' + index);
        }
        tabs.addChangeListener(event -> {
            Screen selected = Screen.values()[tabs.getSelectedIndex()];
            if (state.getScreen() != selected) { controller.navigate(selected); }
        });
        state.addChangeListener(event -> {
            if (tabs.getSelectedIndex() != state.getScreen().ordinal()) {
                tabs.setSelectedIndex(state.getScreen().ordinal());
            }
        });
        add(tabs, BorderLayout.CENTER);
        JLabel footer = Ui.label("CSC207 · Group 17     |     No live elevator status     |     Trips/preferences: session only", 13, false);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));
        add(footer, BorderLayout.SOUTH);
    }

    @Override
    public void close() {
        if (stationMap instanceof StationMapPanel) { ((StationMapPanel) stationMap).close(); }
    }
}
