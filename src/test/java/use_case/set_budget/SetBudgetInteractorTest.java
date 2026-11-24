package use_case.set_budget;

import java.math.BigDecimal;

import data_access.InMemorySetBudgetDataAccessObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SetBudgetInteractorTest {

    @Test
    void successTest() {
        InMemorySetBudgetDataAccessObject dataAccess = new InMemorySetBudgetDataAccessObject();
        SetBudgetInputData inputData = new SetBudgetInputData("Paul", "50", "200");

        SetBudgetOutputBoundary successPresenter = new SetBudgetOutputBoundary() {
            @Override
            public void prepareSuccessView(SetBudgetOutputData outputData) {
                assertEquals("Paul", outputData.getUsername());
                assertEquals("50", outputData.getDailyLimitText());
                assertEquals("200", outputData.getBalanceText());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected.");
            }
        };

        SetBudgetInputBoundary interactor = new SetBudgetInteractor(dataAccess, successPresenter);
        interactor.execute(inputData);

        assertEquals(0, new BigDecimal("50").compareTo(dataAccess.getBudget("Paul").getLimit()));
        assertEquals(0, new BigDecimal("200").compareTo(dataAccess.getWallet("Paul").getBalance()));
    }
}