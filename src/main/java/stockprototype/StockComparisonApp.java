package stockprototype;

import stockprototype.comparison.ComparisonInteractor;
import stockprototype.comparison.ui.ComparisonPanel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/** Starts the two-stock comparison screen. */
public final class StockComparisonApp {
    private StockComparisonApp() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String key = System.getenv("ALPHA_VANTAGE_API_KEY");
                boolean live = selectLiveMode(args, key);
                StockDataSource prices = live ? new AlphaVantageClient(key) : new SampleStockDataSource();
                JFrame frame = new JFrame("Stock comparison");
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.setContentPane(new ComparisonPanel(new ComparisonInteractor(prices), live));
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
            }
            catch (IllegalArgumentException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(),
                        "Stock comparison", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static boolean selectLiveMode(String[] args, String key) {
        if (args.length == 0) {
            return key != null && !key.isBlank();
        }
        if (args.length == 1 && "--demo".equals(args[0])) {
            return false;
        }
        if (args.length == 1 && "--live".equals(args[0])) {
            return true;
        }
        throw new IllegalArgumentException("Use --demo for sample prices or --live for Alpha Vantage.");
    }
}
