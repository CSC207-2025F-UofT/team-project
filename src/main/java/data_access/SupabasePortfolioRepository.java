package data_access;

import entity.Portfolio;

/**
 * Supabase implementation of PortfolioRepository.
 * TODO: Implement actual Supabase database integration.
 */
public class SupabasePortfolioRepository implements PortfolioRepository {
    
    // TODO: Add Supabase client field
    // TODO: Add database connection configuration
    
    public SupabasePortfolioRepository() {
        // TODO: Initialize Supabase client with credentials
    }

    @Override
    public Portfolio findById(String id) {
        // TODO: Query Supabase database for portfolio by ID
        // TODO: Map database result to Portfolio entity
        throw new UnsupportedOperationException("Supabase integration not yet implemented");
    }

    @Override
    public void save(Portfolio portfolio) {
        // TODO: Convert Portfolio entity to database record
        // TODO: Insert or update portfolio in Supabase
        // TODO: Handle positions and trades (related tables)
        throw new UnsupportedOperationException("Supabase integration not yet implemented");
    }

    /**
     * TODO: Implement database schema:
     * - portfolios table (id, owner_id, cash)
     * - positions table (portfolio_id, ticker, quantity, average_cost)
     * - trades table (position_id, trade_id, ticker, quantity, price, timestamp, is_buy)
     */
}
