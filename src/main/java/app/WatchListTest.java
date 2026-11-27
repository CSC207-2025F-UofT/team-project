package app;

import data_access.AccoutWatchListDataAccessObject;
import stockprototype.AlphaVantageClient;
import stockprototype.StockDataSource;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class WatchListTest {

    public static void main(String[] args) {
        String username = "YOUR_ACCOUNT_NAME";   // the one you signed up with
        String key = System.getenv("ALPHA_VANTAGE_API_KEY");

        StockDataSource prices;
        if (key == null || key.isBlank()) {
            prices = symbol -> {
                throw new java.io.IOException("set ALPHA_VANTAGE_API_KEY");
            };
        } else {
            prices = new AlphaVantageClient(key);
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Watchlist test");
            frame.setContentPane(WatchListAppBuilder.createWatchlistView(
                    username, prices, new AccoutWatchListDataAccessObject()));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}