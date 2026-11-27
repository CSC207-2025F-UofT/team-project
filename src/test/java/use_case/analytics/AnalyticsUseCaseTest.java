package use_case.analytics;

import data_access.InMemoryDataAccessObject;
import entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.PhotoAccessException;
import use_case.TripAccessException;
import use_case.dashboard.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test analytics use case generate the correct outputs and invoke failure view in either scenario.
 */
public class AnalyticsUseCaseTest {

    private final CapturingPresenter presenter = new CapturingPresenter();
    private InMemoryDataAccessObject dataAccess;
    private InMemoryDataAccessObject failureDataAccess;

    private static class CapturingPresenter implements DashboardOutputBoundary {
        DashboardOutputData dashboardOutputData;
        String errorMessage;
        @Override
        public void prepareSuccessView(DashboardOutputData dashboardOutputData) {
            this.dashboardOutputData = dashboardOutputData;
        }

        @Override
        public void prepareFailureView(String msg) {
            this.errorMessage = msg;
        }
    }

    @BeforeEach
    void setUp() {
        dataAccess = new InMemoryDataAccessObject();
        failureDataAccess = new InMemoryDataAccessObject() {
            @Override
            public List<Trip> getTrips() {
                throw new TripAccessException("Load trips failed.");
            }
            public List<Photo> getPhotos() {
                throw new PhotoAccessException("Load photos failed.");
            }
        };
        dataAccess.writePhotos(List.of(
                new Photo("a1", ImageType.JPEG, new Location(
                        1, 1, new LocationInfo(
                                "cityA", "countyA", "stateA", "countryA", "continentA", "landmarkA")),
                        LocalDateTime.of(2026, 10, 5, 2, 1)
                ),
                new Photo("a2", ImageType.JPEG, new Location(
                        1, 1, new LocationInfo(
                        "cityA", "countyA", "stateA", "countryA", "continentA", "landmarkA")),
                        LocalDateTime.of(2026, 11, 5, 2, 1)
                ),
                new Photo("b1", ImageType.JPEG, new Location(
                        2, 2, new LocationInfo(
                        "cityB", "countyA", "stateA", "countryA", "continentA", "landmarkA")),
                        LocalDateTime.of(2026, 11, 5, 2, 1)
                )
        ));
        dataAccess.writeTrips(List.of(
                new Trip("1", "to-city-A", LocalDateTime.of(2026, 10, 1, 1, 1), LocalDateTime.of(2026, 10, 7, 2, 1)),
                new Trip("2", "to-city-A-B", LocalDateTime.of(2026, 11, 1, 1, 1), LocalDateTime.of(2026, 10, 7, 2, 1))
                ));
        dataAccess.getTripById("1").addPhoto(dataAccess.getPhotoById("a1"));
        dataAccess.getTripById("2").addPhoto(dataAccess.getPhotoById("a2"));
        dataAccess.getTripById("2").addPhoto(dataAccess.getPhotoById("b1"));
    }

    @AfterEach
    void tearDown() {
        dataAccess = null;
        failureDataAccess = null;
    }

    /**
     * Test the output data is correct in type, content, and the inner ranking. City A appears
     * in both trips while City B only once, so City A should appear in the first item
     * in item.visits.
     */
    @Test
    void mostVisitedCitySuccessAndCorrectOrder() {
        AnalyticsUseCase useCase = new AnalyticsUseCase(dataAccess, dataAccess, presenter);
        DashboardInputDataCatalog.MostVisitedInputData inputData = new DashboardInputDataCatalog.MostVisitedInputData(
                Optional.of(LocationInfoType.CITY)
        );
        useCase.execute(inputData);
        DashboardOutputData outputData = presenter.dashboardOutputData;
        assertNotNull(outputData);

        List<DashboardItem> items = outputData.getDashboardItems();
        assertEquals(1, items.size());

        DashboardOutputDataCatalog.MostVisited item = (DashboardOutputDataCatalog.MostVisited) items.getFirst();
        assertInstanceOf(DashboardOutputDataCatalog.MostVisited.class, item);

        assertEquals(LocationInfoType.CITY, item.typeOfPlace());
        List<DashboardOutputDataCatalog.Visit> visits = item.placeVisits();
        assertEquals(2, visits.size());

        DashboardOutputDataCatalog.Visit visit = visits.getFirst();
        assertInstanceOf(DashboardOutputDataCatalog.Visit.class, visit);
        assertEquals("cityA", visit.place());
    }

    @Test
    void mostVisitedCityFailureAndInvokedFailureView() {
        AnalyticsUseCase useCase = new AnalyticsUseCase(failureDataAccess, failureDataAccess, presenter);
        DashboardInputDataCatalog.MostVisitedInputData inputData = new DashboardInputDataCatalog.MostVisitedInputData(
                Optional.of(LocationInfoType.CITY)
        );
        useCase.execute(inputData);
        assertNull(presenter.dashboardOutputData);
        assertNotNull(presenter.errorMessage);
    }
}
