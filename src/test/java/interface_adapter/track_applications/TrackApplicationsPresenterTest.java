package interface_adapter.track_applications;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import use_case.track_applications.ApplicationSummary;
import use_case.track_applications.TrackApplicationsOutputData;

class TrackApplicationsPresenterTest {

    private TrackApplicationsViewModel viewModel;
    private TrackApplicationsPresenter presenter;
    private int notifications;

    @BeforeEach
    void setUp() {
        viewModel = new TrackApplicationsViewModel();
        viewModel.addListener(new TrackApplicationsViewModelListener() {
            @Override
            public void stateChanged() {
                notifications++;
            }
        });
        presenter = new TrackApplicationsPresenter(viewModel);
    }

    @Test
    void successFormatsRowsAndNotifiesTheView() {
        final List<ApplicationSummary> summaries = new ArrayList<>();
        summaries.add(new ApplicationSummary("a1", "Mochi", LocalDate.of(2026, 11, 16), "INTERVIEW",
                "Call us on Friday"));
        summaries.add(new ApplicationSummary("a2", "Tofu", LocalDate.of(2026, 11, 17), "PENDING", ""));

        presenter.prepareSuccessView(new TrackApplicationsOutputData(summaries));

        final List<ApplicationRow> rows = viewModel.getState().getRows();
        assertEquals(2, rows.size());
        assertEquals("Mochi", rows.get(0).getPetName());
        assertEquals("2026-11-16", rows.get(0).getSubmittedAt());
        assertEquals("INTERVIEW", rows.get(0).getStatus());
        assertEquals("Call us on Friday", rows.get(0).getAdminNote());
        assertEquals(TrackApplicationsPresenter.NO_NOTE, rows.get(1).getAdminNote());
        assertEquals(TrackApplicationsPresenter.SELECT_HINT, viewModel.getState().getMessage());
        assertEquals(1, notifications);
    }

    @Test
    void emptyListShowsMessage() {
        presenter.prepareSuccessView(new TrackApplicationsOutputData(new ArrayList<>()));

        assertTrue(viewModel.getState().getRows().isEmpty());
        assertEquals(TrackApplicationsPresenter.NO_APPLICATIONS, viewModel.getState().getMessage());
    }

    @Test
    void failureClearsRowsAndShowsTheError() {
        final List<ApplicationSummary> summaries = new ArrayList<>();
        summaries.add(new ApplicationSummary("a1", "Mochi", LocalDate.of(2026, 11, 16), "PENDING", ""));
        presenter.prepareSuccessView(new TrackApplicationsOutputData(summaries));

        presenter.prepareFailView("Please log in to see your applications.");

        assertTrue(viewModel.getState().getRows().isEmpty());
        assertEquals("Please log in to see your applications.", viewModel.getState().getMessage());
        assertEquals(2, notifications);
    }
}
