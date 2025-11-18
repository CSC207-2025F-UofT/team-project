package game.use_case.GetPetFact;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

// game.data_access.CompositePetFactDataAccessObject;


class GetPetFactInteractorTest {

    private static class StubPetFactDAO implements PetFactDataAccessInterface {
        private final Optional<GetPetFactOutputData> resultToReturn;
        String lastSpeciesCalled;
        String lastBreedCalled;

        StubPetFactDAO(Optional<GetPetFactOutputData> resultToReturn) {
            this.resultToReturn = resultToReturn;
        }

        @Override
        public Optional<GetPetFactOutputData> fetchFact(String species, String breed) {
            this.lastSpeciesCalled = species;
            this.lastBreedCalled = breed;
            return resultToReturn;
        }
    }

    /**
     * Stub presenter: just stores the last output data it received.
     */
    private static class StubPresenter implements GetPetFactOutputBoundary {
        GetPetFactOutputData lastOutput;

        @Override
        public void present(GetPetFactOutputData outputData) {
            this.lastOutput = outputData;
        }
    }

    @Test
    void successDogFactTest() {
        // Arrange
        GetPetFactOutputData daoResult =
                new GetPetFactOutputData("Beagle dog fact", "DogAPI", true);
        StubPetFactDAO dao = new StubPetFactDAO(Optional.of(daoResult));
        StubPresenter presenter = new StubPresenter();

        GetPetFactInputBoundary interactor = new GetPetFactInteractor(dao, presenter);

        // Act
        interactor.execute("dog", "Beagle");

        // Assert DAO was called correctly
        assertEquals("dog", dao.lastSpeciesCalled);
        assertEquals("Beagle", dao.lastBreedCalled);

        // Assert presenter received the expected output data
        assertNotNull(presenter.lastOutput);
        assertEquals("Beagle dog fact", presenter.lastOutput.getFactText());
        assertEquals("DogAPI", presenter.lastOutput.getSource());
        assertTrue(presenter.lastOutput.isSuccess());
    }

    @Test
    void successCatFactTest() {
        // Arrange
        GetPetFactOutputData daoResult =
                new GetPetFactOutputData("Siamese cat fact", "CatAPI", true);
        StubPetFactDAO dao = new StubPetFactDAO(Optional.of(daoResult));
        StubPresenter presenter = new StubPresenter();

        GetPetFactInputBoundary interactor = new GetPetFactInteractor(dao, presenter);

        // Act
        interactor.execute("cat", "Siamese");

        // Assert DAO was called correctly
        assertEquals("cat", dao.lastSpeciesCalled);
        assertEquals("Siamese", dao.lastBreedCalled);

        // Assert presenter received the expected output data
        assertNotNull(presenter.lastOutput);
        assertEquals("Siamese cat fact", presenter.lastOutput.getFactText());
        assertEquals("CatAPI", presenter.lastOutput.getSource());
        assertTrue(presenter.lastOutput.isSuccess());
    }

    @Test
    void failureNoFactReturnedTest() {
        // Arrange: DAO returns Optional.empty()
        StubPetFactDAO dao = new StubPetFactDAO(Optional.empty());
        StubPresenter presenter = new StubPresenter();

        GetPetFactInputBoundary interactor = new GetPetFactInteractor(dao, presenter);

        // Act
        interactor.execute("dog", "UnknownBreed");

        // Assert DAO was called
        assertEquals("dog", dao.lastSpeciesCalled);
        assertEquals("UnknownBreed", dao.lastBreedCalled);

        // Interactor should create the default failure output
        assertNotNull(presenter.lastOutput);
        assertEquals("", presenter.lastOutput.getFactText());
        assertEquals("", presenter.lastOutput.getSource());
        assertFalse(presenter.lastOutput.isSuccess());
    }
}
