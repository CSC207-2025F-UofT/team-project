package data_access.portfolio;

import entity.Holding;
import entity.Portfolio;
import entity.Transaction;
import use_case.portfolio.PortfolioDataAccessInterface;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * SQLite data-access object for user portfolios.
 */
public final class PortfolioDataAccessObject implements PortfolioDataAccessInterface {
    public PortfolioDataAccessObject() throws SQLException {
        DatabaseConnection.initializeDatabase();
    }

    @Override
    public Portfolio loadPortfolio(String username) {
        validateUsername(username);
        try (Connection connection = DatabaseConnection.getConnection()) {
            BigDecimal availableCash = loadAvailableCash(connection, username);
            if (availableCash == null) {
                return null;
            }

            Map<String, Holding> holdings = loadHoldings(connection, username);
            List<Transaction> transactions = loadTransactions(connection, username);
            return Portfolio.restore(
                    Portfolio.DEFAULT_INITIAL_BALANCE,
                    availableCash,
                    holdings,
                    transactions);
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to load portfolio", exception);
        }
    }

    @Override
    public void savePortfolio(String username, Portfolio portfolio) {
        validateUsername(username);
        if (portfolio == null) {
            throw new IllegalArgumentException("portfolio must not be null");
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                savePortfolioBalance(connection, username, portfolio);
                saveHoldings(connection, username, portfolio);
                saveTransactions(connection, username, portfolio);
                connection.commit();
            } catch (SQLException exception) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }
                throw new RuntimeException("Failed to save portfolio", exception);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to connect to portfolio database", exception);
        }
    }

    private static void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("username must not be empty");
        }
    }

    private BigDecimal loadAvailableCash(
            Connection connection,
            String username) throws SQLException {
        String sql = "SELECT available_cash FROM portfolios WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("available_cash");
                }
                return null;
            }
        }
    }

    private Map<String, Holding> loadHoldings(
            Connection connection,
            String username) throws SQLException {
        Map<String, Holding> holdings = new LinkedHashMap<>();
        String sql = "SELECT ticker, quantity, average_purchase_price, current_price "
                + "FROM holdings WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String ticker = resultSet.getString("ticker");
                    holdings.put(ticker, new Holding(
                            ticker,
                            resultSet.getInt("quantity"),
                            resultSet.getBigDecimal("average_purchase_price"),
                            resultSet.getBigDecimal("current_price")));
                }
            }
        }
        return holdings;
    }

    private void savePortfolioBalance(
            Connection connection,
            String username,
            Portfolio portfolio) throws SQLException {
        String sql = "INSERT INTO portfolios (username, available_cash) "
                + "VALUES (?, ?) "
                + "ON CONFLICT(username) DO UPDATE SET "
                + "available_cash = excluded.available_cash";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setBigDecimal(2, portfolio.getAvailableCash());
            statement.executeUpdate();
        }
    }

    private void saveHoldings(
            Connection connection,
            String username,
            Portfolio portfolio) throws SQLException {
        String deleteSql = "DELETE FROM holdings WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteSql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        }

        String insertSql = "INSERT INTO holdings "
                + "(username, ticker, quantity, average_purchase_price, current_price) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            for (Holding holding : portfolio.getHoldings().values()) {
                statement.setString(1, username);
                statement.setString(2, holding.getTicker());
                statement.setInt(3, holding.getQuantity());
                statement.setBigDecimal(4, holding.getAveragePurchasePrice());
                statement.setBigDecimal(5, holding.getCurrentPrice());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void saveTransactions(
            Connection connection,
            String username,
            Portfolio portfolio) throws SQLException {
        String deleteSql = "DELETE FROM transactions WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteSql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        }

        String insertSql = "INSERT INTO transactions "
                + "(username, ticker, type, quantity, price, timestamp) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            for (Transaction transaction : portfolio.getTransactions()) {
                statement.setString(1, username);
                statement.setString(2, transaction.getTicker());
                statement.setString(3, transaction.getType().name());
                statement.setInt(4, transaction.getQuantity());
                statement.setBigDecimal(5, transaction.getPrice());
                statement.setString(6, transaction.getTimestamp().toString());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private List<Transaction> loadTransactions(
            Connection connection,
            String username) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT ticker, type, quantity, price, timestamp "
                + "FROM transactions WHERE username = ? ORDER BY id";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction.Type type = Transaction.Type.valueOf(
                            resultSet.getString("type"));
                    transactions.add(new Transaction(
                            resultSet.getString("ticker"),
                            resultSet.getInt("quantity"),
                            resultSet.getBigDecimal("price"),
                            type,
                            Instant.parse(resultSet.getString("timestamp"))));
                }
            }
        }
        return transactions;
    }
}
