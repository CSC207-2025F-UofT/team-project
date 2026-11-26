package interface_adapter.communicate_request;

import org.junit.jupiter.api.Test;
import use_case.communicate_request.CommunicateRequestOutputData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommunicateRequestPresenterTest {

    @Test
    void successUpdatesViewModel() {
        CommunicateRequestViewModel viewModel =
                new CommunicateRequestViewModel();

        CommunicateRequestPresenter presenter =
                new CommunicateRequestPresenter(viewModel);

        CommunicateRequestOutputData output =
                new CommunicateRequestOutputData(
                        List.of(
                                new CommunicateRequestOutputData.PictogramItem(
                                        1,
                                        "water",
                                        "water.png")),
                        "I want water.",
                        true);

        presenter.prepareSuccessView(output);

        CommunicateRequestState state =
                viewModel.getState();

        assertEquals(
                "I want water.",
                state.getPreviewText());

        assertEquals(
                1,
                state.getPictograms().size());

        assertEquals(
                "water",
                state.getPictograms().get(0).getLabel());

        assertTrue(state.hasSelection());
        assertNull(state.getErrorMessage());
    }

    @Test
    void failureUpdatesErrorMessage() {
        CommunicateRequestViewModel viewModel =
                new CommunicateRequestViewModel();

        CommunicateRequestPresenter presenter =
                new CommunicateRequestPresenter(viewModel);

        presenter.prepareFailView("error");

        assertEquals(
                "error",
                viewModel.getState().getErrorMessage());
    }
}
