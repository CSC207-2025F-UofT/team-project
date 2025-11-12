package data_access;

import java.util.HashMap;
import java.util.Map;

import entity.Portfolio;

/**
 * In-memory implementation of PortfolioRepository for testing and development.
 */
public class InMemoryPortfolioRepository implements PortfolioRepository {
    private final Map<String, Portfolio> portfolios;

    public InMemoryPortfolioRepository() {
        this.portfolios = new HashMap<>();
    }

    @Override
    public Portfolio findById(String id) {
        return portfolios.get(id);
    }

    @Override
    public void save(Portfolio portfolio) {
        portfolios.put(portfolio.getId(), portfolio);
    }

    /**
     * TODO: Implement findByOwnerId to get all portfolios for a user
     */
    public Portfolio[] findByOwnerId(String ownerId) {
        return portfolios.values().stream()
                .filter(p -> p.getOwnerId().equals(ownerId))
                .toArray(Portfolio[]::new);
    }

    /**
     * Clear all portfolios (useful for testing).
     */
    public void clear() {
        portfolios.clear();
    }
}
