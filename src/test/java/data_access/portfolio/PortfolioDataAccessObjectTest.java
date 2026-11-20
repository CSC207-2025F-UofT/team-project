package data_access.portfolio;

import entity.Holding;
import entity.Portfolio;
import entity.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PortfolioDataAccessObjectTest {
    private PortfolioDataAccessObject dao;

    @BeforeEach
    void setUp() throws SQLException, IOException {
        deleteDatabase();
        dao = new PortfolioDataAccessObject();
    }

    @AfterEach
    void tearDown() throws IOException {
        deleteDatabase();
    }

    private void deleteDatabase() throws IOException {
        Files.deleteIfExists(Path.of("portfolio.db"));
    }

    private void assertMoney(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual));
    }

    @Test
    void saveAndLoadPortfolioRestoresAllData() {
        Portfolio original = new Portfolio();
        original.buy("AAPL", 3, new BigDecimal("100.00"));
        original.sell("AAPL", 1, new BigDecimal("120.00"));
        original.updateCurrentPrice("AAPL", new BigDecimal("125.00"));

        dao.savePortfolio("testuser", original);

        Portfolio loaded = dao.loadPortfolio("testuser");

        assertNotNull(loaded);
        assertMoney("10000.00", loaded.getInitialBalance());
        assertMoney("9820.00", loaded.getAvailableCash());
        assertEquals(1, loaded.getHoldings().size());

        Holding holding = loaded.getHoldings().get("AAPL");
        assertNotNull(holding);
        assertEquals(2, holding.getQuantity());
        assertMoney("100.00", holding.getAveragePurchasePrice());
        assertMoney("125.00", holding.getCurrentPrice());

        assertEquals(2, loaded.getTransactions().size());
        Transaction buy = loaded.getTransactions().get(0);
        assertEquals(Transaction.Type.BUY, buy.getType());
        assertEquals("AAPL", buy.getTicker());
        assertEquals(3, buy.getQuantity());
        assertMoney("100.00", buy.getPrice());

        Transaction sell = loaded.getTransactions().get(1);
        assertEquals(Transaction.Type.SELL, sell.getType());
        assertEquals("AAPL", sell.getTicker());
        assertEquals(1, sell.getQuantity());
        assertMoney("120.00", sell.getPrice());
        assertEquals(original.getTransactions().get(0).getTimestamp(), buy.getTimestamp());
        assertEquals(original.getTransactions().get(1).getTimestamp(), sell.getTimestamp());
    }

    @Test
    void savingAgainUpdatesExistingPortfolio() {
        Portfolio portfolio = new Portfolio();
        portfolio.buy("AAPL", 2, new BigDecimal("150.00"));
        dao.savePortfolio("testuser", portfolio);

        portfolio.buy("AAPL", 1, new BigDecimal("180.00"));
        dao.savePortfolio("testuser", portfolio);

        Portfolio loaded = dao.loadPortfolio("testuser");
        assertNotNull(loaded);
        assertMoney("9520.00", loaded.getAvailableCash());
        Holding holding = loaded.getHoldings().get("AAPL");
        assertNotNull(holding);
        assertEquals(3, holding.getQuantity());
        assertMoney("160.00", holding.getAveragePurchasePrice());
        assertEquals(2, loaded.getTransactions().size());
    }

    @Test
    void loadPortfolioReturnsNullForUnknownUser() {
        assertNull(dao.loadPortfolio("unknownuser"));
    }

    @Test
    void saveAndLoadEmptyPortfolio() {
        dao.savePortfolio("testuser", new Portfolio());

        Portfolio loaded = dao.loadPortfolio("testuser");
        assertNotNull(loaded);
        assertMoney("10000.00", loaded.getAvailableCash());
        assertTrue(loaded.getHoldings().isEmpty());
        assertTrue(loaded.getTransactions().isEmpty());
    }

    @Test
    void differentUsersHaveSeparatePortfolios() {
        Portfolio firstPortfolio = new Portfolio();
        firstPortfolio.buy("AAPL", 1, new BigDecimal("100.00"));
        Portfolio secondPortfolio = new Portfolio();
        secondPortfolio.buy("MSFT", 1, new BigDecimal("200.00"));

        dao.savePortfolio("user1", firstPortfolio);
        dao.savePortfolio("user2", secondPortfolio);

        Portfolio loadedFirst = dao.loadPortfolio("user1");
        Portfolio loadedSecond = dao.loadPortfolio("user2");
        assertNotNull(loadedFirst);
        assertNotNull(loadedSecond);
        assertMoney("9900.00", loadedFirst.getAvailableCash());
        assertMoney("9800.00", loadedSecond.getAvailableCash());
        assertTrue(loadedFirst.getHoldings().containsKey("AAPL"));
        assertFalse(loadedFirst.getHoldings().containsKey("MSFT"));
        assertTrue(loadedSecond.getHoldings().containsKey("MSFT"));
        assertFalse(loadedSecond.getHoldings().containsKey("AAPL"));
    }

    @Test
    void savingEmptyPortfolioReplacesPreviousData() {
        Portfolio original = new Portfolio();
        original.buy("AAPL", 2, new BigDecimal("150.00"));
        dao.savePortfolio("testuser", original);

        dao.savePortfolio("testuser", new Portfolio());

        Portfolio loaded = dao.loadPortfolio("testuser");
        assertNotNull(loaded);
        assertMoney("10000.00", loaded.getAvailableCash());
        assertTrue(loaded.getHoldings().isEmpty());
        assertTrue(loaded.getTransactions().isEmpty());
    }

    @Test
    void databaseRejectsHoldingsForUnknownUser() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO holdings "
                             + "(username, ticker, quantity, "
                             + "average_purchase_price, current_price) "
                             + "VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, "unknownuser");
            statement.setString(2, "AAPL");
            statement.setInt(3, 1);
            statement.setBigDecimal(4, new BigDecimal("100.00"));
            statement.setBigDecimal(5, new BigDecimal("100.00"));

            assertThrows(SQLException.class, statement::executeUpdate);
        }
    }

    @Test
    void failedTransactionSaveRollsBackEarlierChanges() throws SQLException {
        Portfolio original = new Portfolio();
        original.buy("AAPL", 1, new BigDecimal("100.00"));
        dao.savePortfolio("testuser", original);

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TRIGGER fail_transaction_insert "
                            + "BEFORE INSERT ON transactions "
                            + "BEGIN SELECT RAISE(ABORT, 'forced failure'); END");
        }

        Portfolio updated = new Portfolio();
        updated.buy("MSFT", 1, new BigDecimal("200.00"));

        assertThrows(RuntimeException.class,
                () -> dao.savePortfolio("testuser", updated));

        Portfolio loaded = dao.loadPortfolio("testuser");
        assertNotNull(loaded);
        assertMoney("9900.00", loaded.getAvailableCash());
        assertTrue(loaded.getHoldings().containsKey("AAPL"));
        assertFalse(loaded.getHoldings().containsKey("MSFT"));
        assertEquals(1, loaded.getTransactions().size());
    }
}
