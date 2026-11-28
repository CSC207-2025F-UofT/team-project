package interface_adapter.meal_planning;

import org.junit.jupiter.api.Test;
import use_case.meal_planning.CreatePlanOutputData;

import java.beans.PropertyChangeEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreatePlanPresenterTest {

    @Test
    void successClearsPreviousErrorAndNotifiesListeners() {
        CreatePlanViewModel viewModel = new CreatePlanViewModel();
        CreatePlanPresenter presenter = new CreatePlanPresenter(viewModel);

        List<PropertyChangeEvent> events = new ArrayList<>();
        viewModel.addPropertyChangeListener(events::add);

        // First attempt fails.
        presenter.prepareFailView("Please select a date.");

        // A later attempt succeeds.
        LocalDate date = LocalDate.of(2026, 11, 18);
        presenter.prepareSuccessView(
                new CreatePlanOutputData("plan-1", date));



        assertEquals("plan-1", viewModel.getPlanId());
        assertEquals(date, viewModel.getDate());
        assertEquals("", viewModel.getErrorMessage());

        assertEquals(2, events.size());
        assertEquals("createPlan", events.get(1).getPropertyName());
        assertSame(viewModel, events.get(1).getNewValue());
    }

    @Test
    void failureClearsPreviousSuccessAndNotifiesListeners() {
        CreatePlanViewModel viewModel = new CreatePlanViewModel();
        CreatePlanPresenter presenter = new CreatePlanPresenter(viewModel);

        List<PropertyChangeEvent> events = new ArrayList<>();
        viewModel.addPropertyChangeListener(events::add);

        // First attempt succeeds.
        presenter.prepareSuccessView(
                new CreatePlanOutputData(
                        "plan-1", LocalDate.of(2026, 11, 18)));

        // A later attempt fails.
        presenter.prepareFailView(
                "A meal plan already exists for this date.");



        assertNull(viewModel.getPlanId());
        assertNull(viewModel.getDate());
        assertEquals(
                "A meal plan already exists for this date.",
                viewModel.getErrorMessage());

        assertEquals(2, events.size());
        assertEquals("createPlan", events.get(1).getPropertyName());
        assertSame(viewModel, events.get(1).getNewValue());
    }
}