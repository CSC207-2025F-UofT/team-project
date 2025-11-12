package data_access;

import entity.Portfolio;

/**
 * Repository interface for Portfolio persistence.
 */
public interface PortfolioRepository {
    /**
     * Find a portfolio by its ID.
     * @param id Portfolio ID
     * @return Portfolio object or null if not found
     */
    Portfolio findById(String id);

    /**
     * Save or update a portfolio.
     * @param portfolio Portfolio to save
     */
    void save(Portfolio portfolio);

    // TODO: Add method to find portfolios by owner ID
    // TODO: Add method to delete a portfolio
}
