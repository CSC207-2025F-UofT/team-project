package org.accessgraph.interface_adapter;

import org.accessgraph.entity.GeoPosition;
import org.accessgraph.use_case.station_map.LocationGateway;
import org.accessgraph.use_case.station_map.MapSession;

import java.io.IOException;
import java.net.URI;
import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/** Translates the browser permission lifecycle into map-session updates. Call on the UI thread. */
public final class LocationController {
    public enum Status { OFF, WAITING, SHARING, DENIED, UNAVAILABLE, BROWSER_ERROR }

    private final MapSession session;
    private final LocationGateway locations;
    private final BrowserGateway browser;
    private final Clock clock;
    private final Executor dispatch;
    private final Runnable mapChanged;
    private final Consumer<Status> showStatus;
    private URI permissionPage;
    private long generation;

    public LocationController(MapSession session, LocationGateway locations, BrowserGateway browser,
                              Clock clock, Executor dispatch, Runnable mapChanged, Consumer<Status> showStatus) {
        this.session = session;
        this.locations = locations;
        this.browser = browser;
        this.clock = clock;
        this.dispatch = dispatch;
        this.mapChanged = mapChanged;
        this.showStatus = showStatus;
    }

    public void locate() {
        if (session.recenter(clock.instant())) {
            mapChanged.run();
            return;
        }
        session.centerOnNextFix();
        try {
            if (permissionPage == null) {
                long current = ++generation;
                permissionPage = locations.start(new LocationGateway.Listener() {
                    @Override public void onPosition(GeoPosition fix) {
                        dispatch.execute(() -> {
                            if (current != generation || !fix.isRecent(clock.instant())) { return; }
                            if (session.accept(fix, clock.instant())) { mapChanged.run(); }
                            showStatus.accept(Status.SHARING);
                        });
                    }

                    @Override public void onFailure(LocationGateway.Failure failure) {
                        dispatch.execute(() -> {
                            if (current == generation) { fail(failure); }
                        });
                    }
                });
            }
            browser.open(permissionPage);
            showStatus.accept(Status.WAITING);
        } catch (IOException exception) {
            stop(Status.BROWSER_ERROR);
        }
    }

    public void expire() {
        if (session.expire(clock.instant())) {
            mapChanged.run();
            showStatus.accept(Status.UNAVAILABLE);
        }
    }

    public void stop() { stop(Status.OFF); }

    private void fail(LocationGateway.Failure failure) {
        switch (failure) {
            case PERMISSION_DENIED:
                stop(Status.DENIED);
                break;
            case STOPPED:
                stop();
                break;
            default:
                if (session.clearPosition()) { mapChanged.run(); }
                showStatus.accept(Status.UNAVAILABLE);
        }
    }

    private void stop(Status status) {
        generation++;
        if (permissionPage != null) {
            permissionPage = null;
            locations.stop();
        }
        if (session.clearPosition()) { mapChanged.run(); }
        showStatus.accept(status);
    }
}
