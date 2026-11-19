package org.accessgraph.view;

import org.accessgraph.data_access.BundledStationRepository;
import org.accessgraph.data_access.BrowserLocationGateway;
import org.accessgraph.entity.Station;
import org.accessgraph.use_case.station_map.MapImageGateway;
import org.accessgraph.use_case.station_map.MapRequest;
import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StationMapPanelTest {
    @Test
    void directoryAndBrowserLinksWorkWithoutAnApiKey() throws Exception {
        List<Station> stations = new BundledStationRepository().load();
        AtomicReference<URI> opened = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            StationMapPanel panel = new StationMapPanel(stations, new FakeMaps(), opened::set, new BrowserLocationGateway());
            JList<?> list = find(panel, JList.class).get(0);
            assertEquals(stations.stream().filter(s -> s.getAccessibility() == Station.Accessibility.CONFIRMED)
                    .count(), list.getModel().getSize());
            assertTrue(button(panel, "Show listed stations").isEnabled());
            assertFalse(button(panel, "Open selected in OpenStreetMap").isEnabled());
            search(panel).setText("Union");
            assertEquals(1, list.getModel().getSize());
            list.setSelectedIndex(0);
            button(panel, "Open selected in OpenStreetMap").doClick();
            assertEquals("www.openstreetmap.org", opened.get().getHost());
            button(panel, "View TTC station details").doClick();
            assertTrue(opened.get().getPath().endsWith("/union-station"));

            search(panel).setText("Old Mill");
            assertEquals(0, list.getModel().getSize(), "Limited stations are excluded by default.");
            assertFalse(button(panel, "Open selected in OpenStreetMap").isEnabled());
            find(panel, JCheckBox.class).get(0).doClick();
            assertEquals(1, list.getModel().getSize());
        });
    }

    @Test
    void firstShowingLoadsTheMapWithoutAnActionOrCredential() throws Exception {
        List<Station> stations = new BundledStationRepository().load();
        FakeMaps gateway = new FakeMaps();
        AtomicReference<StationMapPanel> view = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            StationMapPanel panel = new StationMapPanel(stations, gateway, ignored -> { }, new BrowserLocationGateway());
            view.set(panel);
            panel.addNotify();
        });
        try {
            assertTrue(gateway.started.await(3, TimeUnit.SECONDS));
            SwingUtilities.invokeAndWait(() -> view.get().removeNotify());
            gateway.release.countDown();
            awaitNotice(view.get(), "Showing all 103 listed station pins.");
        } finally {
            SwingUtilities.invokeAndWait(() -> {
                if (view.get().isDisplayable()) { view.get().removeNotify(); }
                view.get().close();
            });
        }
    }

    @Test
    void mapLoadingRunsOffTheEventThreadAndFiltersCancelStaleLoads() throws Exception {
        List<Station> stations = new BundledStationRepository().load();
        FakeMaps gateway = new FakeMaps();
        AtomicReference<StationMapPanel> view = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            StationMapPanel panel = new StationMapPanel(stations, gateway, ignored -> { }, new BrowserLocationGateway());
            view.set(panel);
            button(panel, "Show listed stations").doClick();
            assertFalse(button(panel, "Show listed stations").isEnabled());
        });
        assertTrue(gateway.started.await(3, TimeUnit.SECONDS));
        assertFalse(gateway.calledOnEdt.get());
        SwingUtilities.invokeAndWait(() -> search(view.get()).setText("Union"));
        gateway.release.countDown();
        assertTrue(gateway.finished.await(3, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> {
            assertEquals(1, find(view.get(), JList.class).get(0).getModel().getSize());
            assertTrue(button(view.get(), "Show listed stations").isEnabled());
            assertFalse(find(view.get(), JTextArea.class).stream()
                    .anyMatch(text -> text.getText().startsWith("Showing all")));
        });
    }

    @Test
    void completedMapsAndNetworkErrorsUpdateTheUiWithoutLosingTheDirectory() throws Exception {
        for (boolean fail : new boolean[]{false, true}) {
            FakeMaps gateway = new FakeMaps();
            gateway.fail = fail;
            gateway.release.countDown();
            AtomicReference<StationMapPanel> view = new AtomicReference<>();
            List<Station> stations = new BundledStationRepository().load();
            SwingUtilities.invokeAndWait(() -> {
                StationMapPanel panel = new StationMapPanel(stations, gateway, ignored -> { }, new BrowserLocationGateway());
                view.set(panel);
                search(panel).setText("Union");
                button(panel, "Show listed stations").doClick();
            });
            assertTrue(gateway.finished.await(3, TimeUnit.SECONDS));
            awaitNotice(view.get(), fail ? "Test map unavailable" : "Showing all 1 listed station pins.");
            SwingUtilities.invokeAndWait(() -> {
                assertTrue(button(view.get(), "Show listed stations").isEnabled());
                assertEquals(1, find(view.get(), JList.class).get(0).getModel().getSize());
            });
        }
    }

    private void awaitNotice(StationMapPanel panel, String expected) throws Exception {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);
        AtomicBoolean found = new AtomicBoolean();
        while (!found.get() && System.nanoTime() < deadline) {
            SwingUtilities.invokeAndWait(() -> found.set(find(panel, JTextArea.class).stream()
                    .anyMatch(text -> expected.equals(text.getText()))));
            if (!found.get()) { Thread.sleep(20); }
        }
        assertTrue(found.get(), "Map status was not presented: " + expected);
    }

    private static final class FakeMaps implements MapImageGateway {
        private final CountDownLatch started = new CountDownLatch(1);
        private final CountDownLatch release = new CountDownLatch(1);
        private final CountDownLatch finished = new CountDownLatch(1);
        private final AtomicBoolean calledOnEdt = new AtomicBoolean();
        private boolean fail;

        @Override public BufferedImage fetch(MapRequest request) throws IOException, InterruptedException {
            calledOnEdt.set(SwingUtilities.isEventDispatchThread());
            started.countDown();
            try {
                if (!release.await(3, TimeUnit.SECONDS)) { throw new IOException("Test request timed out"); }
                if (fail) { throw new IOException("Test map unavailable"); }
                return new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
            } finally {
                finished.countDown();
            }
        }
    }

    private static JButton button(Container root, String text) {
        return find(root, JButton.class).stream().filter(button -> text.equals(button.getText()))
                .findFirst().orElseThrow(() -> new AssertionError("Missing button: " + text));
    }

    private static JTextField search(Container root) {
        return find(root, JTextField.class).stream().filter(field -> "Search stations".equals(
                field.getAccessibleContext().getAccessibleName())).findFirst().orElseThrow();
    }

    private static <T> List<T> find(Container root, Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Component child : root.getComponents()) {
            if (type.isInstance(child)) { result.add(type.cast(child)); }
            if (child instanceof Container) { result.addAll(find((Container) child, type)); }
        }
        return result;
    }
}
