package use_case.portfolio;

import entity.Portfolio;

/**
 * Loads and stores the portfolio belonging to a user.
 */
public interface PortfolioDataAccessInterface {
    Portfolio loadPortfolio(String username);

    void savePortfolio(String username, Portfolio portfolio);
}
