package org.accessgraph.interface_adapter;

import org.accessgraph.entity.GeoPosition;
import org.accessgraph.entity.Station;
import org.accessgraph.use_case.station_map.MapRequest;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/** Browser links require no credentials. */
public final class MapLinks {
    private MapLinks() { }

    public static URI station(Station station) {
        return URI.create("https://www.openstreetmap.org/?mlat=" + station.getLatitude()
                + "&mlon=" + station.getLongitude() + "#map=16/" + station.getLatitude() + "/" + station.getLongitude());
    }

    public static URI journey(GeoPosition origin, String destination) {
        String address = MapRequest.validateDestination(destination);
        if (address.isEmpty()) { throw new IllegalArgumentException("Enter a destination address first."); }
        String url = "https://maps.apple.com/directions?mode=transit&destination=" + encode(address);
        if (origin != null) { url += "&source=" + encode(origin.getCoordinates()); }
        return URI.create(url);
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
