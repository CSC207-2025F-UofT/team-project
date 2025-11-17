package view;

import interface_adapter.ViewRecentFlights.ViewRecentFlightsController;
import entity.AirlineFlight;

import java.util.Scanner;
import java.util.List;

public class ViewRecentFlightsView {
    private final ViewRecentFlightsController controller;

    public ViewRecentFlightsView(ViewRecentFlightsController controller) {
        this.controller = controller;
    }

    public void display() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== View Recent Airline Flights ===");
        System.out.print("Enter airline name: ");
        String airlineName = scanner.nextLine();

        controller.viewRecentFlights(airlineName);
    }


    public void showFlights(List<AirlineFlight> flights) {
        if (flights == null || flights.isEmpty()) {
            System.out.println("No recent flights found for this airline.");
        } else {
            System.out.println("\nRecent Flights:");
            for (AirlineFlight flight : flights) {
                System.out.println("- " + flight.getAirline() + " " + flight.getFlightNumber()
                        + " | From " + flight.getDepartureAirport()
                        + " → " + flight.getArrivalAirport()
                        + " | Status: " + flight.getStatus());
            }
        }
    }
}
