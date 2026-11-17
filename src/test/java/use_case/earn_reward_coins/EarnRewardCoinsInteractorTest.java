package use_case.earn_reward_coins;

import data_access.EarnRewardCoinsDataAccessObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class EarnRewardCoinsInteractorTest {

    private static final String USER_ID = "user1";
    private static final int REWARD = 10;

    // In Toronto, this instant is November 16 at 7:30 PM.
    // Therefore, yesterday is November 15, despite UTC being November 17.
    private final Clock clock = Clock.fixed(
            Instant.parse("2026-11-17T00:30:00Z"),
            ZoneOffset.UTC);

    private final LocalDate yesterday = LocalDate.of(2026, 11, 15);

    private EarnRewardCoinsDataAccessObject dao;
    private TestPresenter presenter;
    private EarnRewardCoinsInteractor interactor;

    @BeforeEach
    void setUp() {
        dao = new EarnRewardCoinsDataAccessObject();
        dao.addUser(USER_ID, ZoneId.of("America/Toronto"));

        presenter = new TestPresenter();

        interactor = new EarnRewardCoinsInteractor(
                dao, presenter, REWARD, clock);
    }

    @Test
    void zeroSpendingEarnsReward() {
        setBudget("20.00");
        // No spending entry: DAO should return zero.

        claim();

        assertNotNull(presenter.result);
        assertNull(presenter.error);
        assertEquals(REWARD, presenter.result.getCoinsEarned());
        assertEquals(REWARD, presenter.result.getNewBalance());
        assertEquals(REWARD, dao.getCoinBalance(USER_ID));
        assertTrue(dao.hasClaimedReward(USER_ID, yesterday));
    }

    @Test
    void spendingBelowBudgetEarnsReward() {
        setBudget("20.00");
        dao.setTotalSpending(
                USER_ID, yesterday, new BigDecimal("19.99"));

        claim();

        assertNotNull(presenter.result);
        assertNull(presenter.error);
        assertEquals(REWARD, dao.getCoinBalance(USER_ID));
    }

    @Test
    void spendingEqualToBudgetIsRejected() {
        setBudget("20.00");
        dao.setTotalSpending(
                USER_ID, yesterday, new BigDecimal("20.00"));

        claim();

        assertRejectedWithoutReward();
    }

    @Test
    void spendingAboveBudgetIsRejected() {
        setBudget("20.00");
        dao.setTotalSpending(
                USER_ID, yesterday, new BigDecimal("20.01"));

        claim();

        assertRejectedWithoutReward();
    }

    @Test
    void missingBudgetIsRejected() {
        claim();

        assertRejectedWithoutReward();
    }

    @Test
    void zeroBudgetIsRejected() {
        setBudget("0.00");

        claim();

        assertRejectedWithoutReward();
    }

    @Test
    void claimingTwiceDoesNotAddMoreCoins() {
        setBudget("20.00");
        claim();

        assertNotNull(presenter.result);

        claim();

        assertNull(presenter.result);
        assertNotNull(presenter.error);
        assertEquals(REWARD, dao.getCoinBalance(USER_ID));
        assertTrue(dao.hasClaimedReward(USER_ID, yesterday));
    }

    @Test
    void earlierClaimDoesNotPreventYesterdaysReward() {
        setBudget("20.00");

        // Seed a reward earned on an earlier day.
        dao.claimReward(USER_ID, yesterday.minusDays(1), REWARD);

        claim();

        assertNotNull(presenter.result);
        assertNull(presenter.error);
        assertEquals(REWARD * 2, dao.getCoinBalance(USER_ID));
        assertTrue(dao.hasClaimedReward(USER_ID, yesterday));
    }

    @Test
    void yesterdayUsesUsersTimeZoneInsteadOfUtc() {
        setBudget("20.00");

        // UTC yesterday is November 16; Toronto yesterday is November 15.
        LocalDate utcYesterday = LocalDate.now(clock).minusDays(1);
        dao.setBudget(USER_ID, utcYesterday, BigDecimal.ZERO);

        claim();

        assertNotNull(presenter.result);
        assertTrue(dao.hasClaimedReward(USER_ID, yesterday));
        assertFalse(dao.hasClaimedReward(USER_ID, utcYesterday));
    }

    private void setBudget(String amount) {
        dao.setBudget(USER_ID, yesterday, new BigDecimal(amount));
    }

    private void claim() {
        interactor.execute(new EarnRewardCoinsInputData(USER_ID));
    }

    private void assertRejectedWithoutReward() {
        assertNull(presenter.result);
        assertNotNull(presenter.error);
        assertEquals(0, dao.getCoinBalance(USER_ID));
        assertFalse(dao.hasClaimedReward(USER_ID, yesterday));
    }

    private static class TestPresenter
            implements EarnRewardCoinsOutputBoundary {

        private EarnRewardCoinsOutputData result;
        private String error;

        @Override
        public void prepareSuccessView(EarnRewardCoinsOutputData outputData) {
            result = outputData;
            error = null;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            result = null;
            error = errorMessage;
        }
    }
}