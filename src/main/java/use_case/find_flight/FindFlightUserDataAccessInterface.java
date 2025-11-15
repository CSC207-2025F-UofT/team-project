package use_case.find_flight;

import entity.Flight;
import java.util.List;

public interface FindFlightUserDataAccessInterface {
    List<Flight> search(String originLocationCode,
                  String destinationLocationCode,
                  String departureDate,
                  int adults,
                  boolean nonstop
                  );
}
