package app;

import java.time.LocalDateTime;

import javax.swing.SwingUtilities;

import data_access.AlphaVantageGateway;
import data_access.InMemoryPortfolioRepository;
import data_access.PortfolioRepository;
import data_access.StockDataGateway;
import entity.Portfolio;
import entity.Position;
import entity.Trade;
import interface_adapter.controller.TradingController;
import interface_adapter.presenter.PortfolioPresenter;
import use_case.track_portfolio.TrackPortfolioInputBoundary;
import use_case.track_portfolio.TrackPortfolioInteractor;
import view.PortfolioPage;

/**
 * The Main class is the entry point of the application.
 * Sets up dependency injection and wires all components together.
 */
public class Main {
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            // Initialize the application with Clean Architecture components
            MainFrame frame = createApplication();
            frame.setVisible(true);
        });
    }

    /**
     * Create and wire up all application components.
     * This is where dependency injection happens.
     */
    private static MainFrame createApplication() {
        // TODO: Replace hardcoded values with configuration
        String portfolioId = "portfolio-001";
        String userId = "user-001";

        // === Data Access Layer ===
        // Create repository (in-memory for now)
        PortfolioRepository portfolioRepository = new InMemoryPortfolioRepository();
        
        // Create stock data gateway
        // TODO: Replace with actual API key from configuration
        StockDataGateway stockDataGateway = new AlphaVantageGateway("DEMO_API_KEY");

        // === Create sample portfolio for demo ===
        createSamplePortfolio(portfolioRepository, portfolioId, userId);

        // === Use Case Layer ===
        // Create controller and presenter (need to be wired after UI is created)
        TradingController controller;
        PortfolioPresenter presenter;
        TrackPortfolioInteractor interactor;

        // === View Layer ===
        // Create UI - note: we need controller first, but controller needs interactor
        // which needs presenter, which needs UI. Break the cycle by creating placeholder
        
        // Create main frame
        MainFrame frame = new MainFrame();
        
        // Create portfolio page (will be added to frame)
        // Temporary controller for initialization
        TrackPortfolioInputBoundary tempBoundary = inputData -> {};
        TradingController tempController = new TradingController(tempBoundary);
        PortfolioPage portfolioPage = new PortfolioPage(tempController, portfolioId, userId);

        // === Wire dependencies properly ===
        // Now create the real components
        presenter = new PortfolioPresenter(portfolioPage);
        interactor = new TrackPortfolioInteractor(portfolioRepository, stockDataGateway, presenter);
        controller = new TradingController(interactor);

        // Update portfolio page with real controller
        // TODO: Add setter method to PortfolioPage to update controller
        
        // Add portfolio page to frame
        frame.setContentPane(portfolioPage);

        // Load initial portfolio data
        controller.viewPortfolio(portfolioId, userId);

        return frame;
    }

    /**
     * Create a sample portfolio with some positions for demonstration.
     * TODO: Remove this and implement proper portfolio creation UI
     */
    private static void createSamplePortfolio(PortfolioRepository repository, 
                                              String portfolioId, String userId) {
        Portfolio portfolio = new Portfolio(portfolioId, userId, 10000.0);

        // Add sample positions
        // AAPL position
        Position aaplPosition = new Position("AAPL");
        aaplPosition.addTrade(new Trade("T001", "AAPL", 10, 150.0, 
                LocalDateTime.now().minusDays(30), true));
        portfolio.addPosition(aaplPosition);

        // GOOGL position
        Position googlPosition = new Position("GOOGL");
        googlPosition.addTrade(new Trade("T002", "GOOGL", 5, 130.0, 
                LocalDateTime.now().minusDays(20), true));
        portfolio.addPosition(googlPosition);

        // MSFT position
        Position msftPosition = new Position("MSFT");
        msftPosition.addTrade(new Trade("T003", "MSFT", 8, 350.0, 
                LocalDateTime.now().minusDays(15), true));
        portfolio.addPosition(msftPosition);

        // Save to repository
        repository.save(portfolio);
    }

    /**
     * TODO: Add configuration loading (API keys, database connections, etc.)
     * TODO: Add proper dependency injection framework (e.g., Spring, Guice)
     * TODO: Add logging configuration
     * TODO: Add error handling for initialization failures
     * TODO: Add command-line argument parsing for different modes (demo, production, etc.)
     */
}
