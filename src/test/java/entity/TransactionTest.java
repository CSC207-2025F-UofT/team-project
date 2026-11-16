package entity;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class TransactionTest {
    @Test
    public void constructorAutomaticallyRecordsTimestamp() {
        Transaction transaction = new Transaction(
                "AAPL", 2, new BigDecimal("100"), Transaction.Type.BUY);

        assertNotNull(transaction.getTimestamp());
        assertEquals("AAPL", transaction.getTicker());
    }

    @Test
    public void constructorPreservesProvidedTimestamp() {
        Instant timestamp = Instant.parse("2026-01-01T00:00:00Z");

        Transaction transaction = new Transaction(
                "AAPL", 2, new BigDecimal("100"), Transaction.Type.SELL, timestamp);

        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    public void constructorRejectsZeroPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Transaction("AAPL", 1, BigDecimal.ZERO, Transaction.Type.BUY));
    }
}
