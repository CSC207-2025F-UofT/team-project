package use_case.log_expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import data_access.InMemoryTripDataAccessObject;
import entity.Expense;
import entity.ExpenseCategory;
import entity.Trip;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.exchange_rate.ExchangeRateDataAccessInterface;
import use_case.exchange_rate.ExchangeRateUnavailableException;

class LogExpenseInteractorTest {

    private static final String TRIP_ID = "tokyo-trip";
    private static final LocalDate START = LocalDate.of(2027, 2, 13);
    private static final LocalDate END = LocalDate.of(2027, 2, 19);

    private TestTripDao tripDao;

    private static class TestTripDao extends InMemoryTripDataAccessObject
            implements LogExpenseDataAccessInterface {}

    @BeforeEach
    void setUp() {
        tripDao = new TestTripDao();
        final Trip trip =
                new Trip(TRIP_ID, "Tokyo Reading Week", "Tokyo", START, END, "CAD", 250.0, "tamima");
        trip.addMember("jadyn");
        tripDao.saveTrip(trip);
        tripDao.setCurrentTripId(TRIP_ID);
    }

    @Test
    void successForeignCurrencyTest() {
        final ExchangeRateDataAccessInterface rateGateway =
                new ExchangeRateDataAccessInterface() {
                    @Override
                    public double getRate(String fromCurrency, String toCurrency) {
                        assertEquals("JPY", fromCurrency);
                        assertEquals("CAD", toCurrency);
                        return 0.008951;
                    }

                    @Override
                    public List<String> getSupportedCurrencies() {
                        return List.of("CAD", "JPY", "USD");
                    }
                };

        final LogExpenseOutputBoundary presenter =
                new LogExpenseOutputBoundary() {
                    @Override
                    public void prepareSuccessView(LogExpenseOutputData outputData) {
                        assertEquals(TRIP_ID, outputData.getTripId());
                        assertEquals("Tokyo Reading Week", outputData.getTripName());
                        assertEquals("CAD", outputData.getHomeCurrency());
                        assertEquals(1, outputData.getExpenses().size());
                        final Expense saved = outputData.getExpenses().get(0);
                        assertEquals("Taxi", saved.getDescription());
                        assertEquals(9000.0, saved.getAmount());
                        assertEquals("JPY", saved.getCurrency());
                        assertEquals(80.56, saved.getAmountInHomeCurrency());
                        assertEquals(ExpenseCategory.TRANSPORT, saved.getCategory());
                        assertEquals("tamima", saved.getPayerUsername());
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        throw new AssertionError("Unexpected failure: " + errorMessage);
                    }
                };

        final LogExpenseInteractor interactor =
                new LogExpenseInteractor(tripDao, rateGateway, presenter);
        interactor.execute(
                new LogExpenseInputData(
                        TRIP_ID, "Taxi", "9000", "JPY", ExpenseCategory.TRANSPORT, "2027-02-13", "tamima"));
        assertEquals(1, tripDao.getExpenses(TRIP_ID).size());
    }

    @Test
    void successSameCurrencySkipsGatewayTest() {
        final ExchangeRateDataAccessInterface failingGateway =
                new ExchangeRateDataAccessInterface() {
                    @Override
                    public double getRate(String fromCurrency, String toCurrency)
                            throws ExchangeRateUnavailableException {
                        throw new ExchangeRateUnavailableException("Should not be called for home currency");
                    }

                    @Override
                    public List<String> getSupportedCurrencies() {
                        return List.of("CAD");
                    }
                };

        final LogExpenseOutputBoundary presenter =
                new LogExpenseOutputBoundary() {
                    @Override
                    public void prepareSuccessView(LogExpenseOutputData outputData) {
                        assertEquals(1, outputData.getExpenses().size());
                        assertEquals(1250.0, outputData.getExpenses().get(0).getAmountInHomeCurrency());
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        throw new AssertionError("Unexpected failure: " + errorMessage);
                    }
                };

        final LogExpenseInteractor interactor =
                new LogExpenseInteractor(tripDao, failingGateway, presenter);
        interactor.execute(
                new LogExpenseInputData(
                        "", "Flight", "1250", "cad", ExpenseCategory.FLIGHT, "2027-02-13", "jadyn"));
    }

    @Test
    void failInvalidAmountTest() {
        final String[] capturedError = new String[1];
        final LogExpenseInteractor interactor =
                new LogExpenseInteractor(tripDao, stubGateway(1.0), captureFail(capturedError));

        interactor.execute(
                new LogExpenseInputData(
                        TRIP_ID, "Taxi", "0", "CAD", ExpenseCategory.TRANSPORT, "2027-02-13", "tamima"));
        assertEquals("Amount must be greater than 0.", capturedError[0]);
        assertEquals(0, tripDao.getExpenses(TRIP_ID).size());
    }

    @Test
    void failNonNumericAmountAndEmptyDescriptionTest() {
        final String[] capturedError = new String[1];
        final LogExpenseInteractor interactor =
                new LogExpenseInteractor(tripDao, stubGateway(1.0), captureFail(capturedError));

        interactor.execute(
                new LogExpenseInputData(
                        TRIP_ID, " ", "20", "CAD", ExpenseCategory.FOOD, "2027-02-13", "tamima"));
        assertEquals("Description cannot be empty.", capturedError[0]);

        interactor.execute(
                new LogExpenseInputData(
                        TRIP_ID, "Ramen", "abc", "CAD", ExpenseCategory.FOOD, "2027-02-13", "tamima"));
        assertEquals("Amount must be a number.", capturedError[0]);
    }

    @Test
    void failDateOutsideTripTest() {
        final String[] capturedError = new String[1];
        final LogExpenseInteractor interactor =
                new LogExpenseInteractor(tripDao, stubGateway(1.0), captureFail(capturedError));

        interactor.execute(
                new LogExpenseInputData(
                        TRIP_ID, "Taxi", "20", "CAD", ExpenseCategory.TRANSPORT, "2027-03-01", "tamima"));
        assertEquals("That date is not part of this trip.", capturedError[0]);
    }

    @Test
    void failNonMemberPayerTest() {
        final String[] capturedError = new String[1];
        final LogExpenseInteractor interactor =
                new LogExpenseInteractor(tripDao, stubGateway(1.0), captureFail(capturedError));

        interactor.execute(
                new LogExpenseInputData(
                        TRIP_ID, "Taxi", "20", "CAD", ExpenseCategory.TRANSPORT, "2027-02-13", "stranger"));
        assertEquals("stranger is not a member of this trip.", capturedError[0]);
    }

    @Test
    void failUnsupportedCurrencyTest() {
        final String[] capturedError = new String[1];
        final LogExpenseInteractor interactor =
                new LogExpenseInteractor(tripDao, stubGateway(1.0), captureFail(capturedError));

        interactor.execute(
                new LogExpenseInputData(
                        TRIP_ID, "Taxi", "20", "ZZZ", ExpenseCategory.TRANSPORT, "2027-02-13", "tamima"));
        assertEquals("Currency ZZZ is not supported.", capturedError[0]);
    }

    @Test
    void failExchangeRateUnavailableTest() {
        final ExchangeRateDataAccessInterface offlineGateway =
                new ExchangeRateDataAccessInterface() {
                    @Override
                    public double getRate(String fromCurrency, String toCurrency)
                            throws ExchangeRateUnavailableException {
                        throw new ExchangeRateUnavailableException("Could not reach the exchange rate service");
                    }

                    @Override
                    public List<String> getSupportedCurrencies() {
                        return List.of("CAD", "JPY");
                    }
                };

        final String[] capturedError = new String[1];
        final LogExpenseInteractor interactor =
                new LogExpenseInteractor(tripDao, offlineGateway, captureFail(capturedError));

        interactor.execute(
                new LogExpenseInputData(
                        TRIP_ID, "Taxi", "9000", "JPY", ExpenseCategory.TRANSPORT, "2027-02-13", "tamima"));
        assertEquals("Couldn't get the exchange rate. Try again.", capturedError[0]);
        assertEquals(0, tripDao.getExpenses(TRIP_ID).size());
    }

    private static ExchangeRateDataAccessInterface stubGateway(double rate) {
        return new ExchangeRateDataAccessInterface() {
            @Override
            public double getRate(String fromCurrency, String toCurrency) {
                return rate;
            }

            @Override
            public List<String> getSupportedCurrencies() {
                return List.of("CAD", "JPY", "USD");
            }
        };
    }

    private static LogExpenseOutputBoundary captureFail(String[] capturedError) {
        return new LogExpenseOutputBoundary() {
            @Override
            public void prepareSuccessView(LogExpenseOutputData outputData) {
                throw new AssertionError("Expected failure, got success");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertNotNull(errorMessage);
                capturedError[0] = errorMessage;
            }
        };
    }
}