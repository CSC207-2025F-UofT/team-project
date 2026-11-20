package org.accessgraph.use_case.plan_route;

import org.accessgraph.entity.AccessibilityProfile;
import org.accessgraph.entity.Connection;
import org.accessgraph.entity.Route;
import org.accessgraph.entity.Station;
import org.accessgraph.entity.TransitNetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

/**
 * Fastest route with Dijkstra's algorithm. A search state is (station, current line, transfers used),
 * so the transfer limit and the "change lines only at usable stations" rule can be enforced.
 * Riding through a station without getting off is always allowed.
 */
public final class DijkstraRouteFinder implements RouteFinder {
    /** Estimated minutes added each time the passenger changes lines. */
    public static final int TRANSFER_MINUTES = 5;

    @Override
    public Optional<Route> findRoute(TransitNetwork network, String originId, String destinationId,
                                     AccessibilityProfile profile) {
        Map<String, Integer> best = new HashMap<>();
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(state -> state.minutes));
        State start = new State(originId, null, 0, 0, null, null);
        queue.add(start);
        best.put(start.key(), 0);

        while (!queue.isEmpty()) {
            State current = queue.poll();
            if (current.minutes > best.getOrDefault(current.key(), Integer.MAX_VALUE)) {
                continue;
            }
            if (current.stationId.equals(destinationId)) {
                return Optional.of(toRoute(network, current));
            }
            for (Connection connection : network.connectionsFrom(current.stationId)) {
                State next = step(network, profile, current, connection);
                if (next != null && next.minutes < best.getOrDefault(next.key(), Integer.MAX_VALUE)) {
                    best.put(next.key(), next.minutes);
                    queue.add(next);
                }
            }
        }
        return Optional.empty();
    }

    /** Returns the state after taking the connection, or null when the profile forbids it. */
    private State step(TransitNetwork network, AccessibilityProfile profile, State current, Connection connection) {
        boolean changesLine = current.line != null && !current.line.equals(connection.getLine());
        int transfers = current.transfers + (changesLine ? 1 : 0);
        if (changesLine && !profile.canUse(network.getStation(current.stationId))) {
            return null;
        }
        if (transfers > profile.getMaxTransfers()) {
            return null;
        }
        int minutes = current.minutes + connection.getMinutes() + (changesLine ? TRANSFER_MINUTES : 0);
        return new State(connection.getToId(), connection.getLine(), transfers, minutes, current,
                connection.getLine());
    }

    private Route toRoute(TransitNetwork network, State end) {
        List<Station> stations = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        for (State state = end; state != null; state = state.previous) {
            stations.add(network.getStation(state.stationId));
            if (state.arrivedBy != null) {
                lines.add(state.arrivedBy);
            }
        }
        Collections.reverse(stations);
        Collections.reverse(lines);
        return new Route(stations, lines, end.minutes, end.transfers);
    }

    private static final class State {
        private final String stationId;
        private final String line;
        private final int transfers;
        private final int minutes;
        private final State previous;
        private final String arrivedBy;

        private State(String stationId, String line, int transfers, int minutes, State previous, String arrivedBy) {
            this.stationId = stationId;
            this.line = line;
            this.transfers = transfers;
            this.minutes = minutes;
            this.previous = previous;
            this.arrivedBy = arrivedBy;
        }

        private String key() {
            return stationId + "|" + line + "|" + transfers;
        }
    }
}
