package use_case.find_flight;


public interface FindFlightUserDataAccessInterface {
    String search(String originLocationCode,
                  String destinationLocationCode,
                  String departureDate,
                  int adults,
                  boolean nonstop
                  );
}
