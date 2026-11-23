package app;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppBuilder()
                .addTripListView()
                .addTripDetailsView()
                .addMapView()
                .addDashBoardView()
                .addManipulateTripsUseCase()
                .build()
                .setVisible(true));
    }
}
