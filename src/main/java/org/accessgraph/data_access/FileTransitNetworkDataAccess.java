package org.accessgraph.data_access;

import org.accessgraph.entity.Connection;
import org.accessgraph.entity.Station;
import org.accessgraph.entity.TransitNetwork;
import org.accessgraph.use_case.plan_route.TransitNetworkDataAccessInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Builds the network from the bundled stations and the connections file (rows work in both directions). */
public final class FileTransitNetworkDataAccess implements TransitNetworkDataAccessInterface {
    private static final String CONNECTIONS = "/data/ttc-connections.tsv";
    private final TransitNetwork network;

    public FileTransitNetworkDataAccess(List<Station> stations) throws IOException {
        Map<String, Station> byName = new HashMap<>();
        for (Station station : stations) {
            byName.put(station.getName(), station);
        }
        this.network = new TransitNetwork(stations, readConnections(byName));
    }

    @Override
    public TransitNetwork getNetwork() {
        return network;
    }

    private List<Connection> readConnections(Map<String, Station> byName) throws IOException {
        InputStream stream = getClass().getResourceAsStream(CONNECTIONS);
        if (stream == null) {
            throw new IOException("The bundled connections file is missing.");
        }
        List<Connection> connections = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String row;
            while ((row = reader.readLine()) != null) {
                if (row.isBlank() || row.startsWith("#") || row.startsWith("line\t")) {
                    continue;
                }
                String[] values = row.split("\t");
                Station from = byName.get(values[1]);
                Station to = byName.get(values[2]);
                if (values.length != 4 || from == null || to == null) {
                    throw new IOException("Invalid connection row: " + row);
                }
                int minutes = Integer.parseInt(values[3]);
                connections.add(new Connection(from.getId(), to.getId(), values[0], minutes));
                connections.add(new Connection(to.getId(), from.getId(), values[0], minutes));
            }
        } catch (NumberFormatException exception) {
            throw new IOException("A connection has an invalid travel time.", exception);
        }
        return connections;
    }
}
