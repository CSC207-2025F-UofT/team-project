package org.example.cocktailcraft.favourite;

import org.example.cocktailcraft.entity.User;
import org.example.cocktailcraft.use_case.favourite.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FavouriteInteractorTest {

    @Test
    void successFavouriteTest() {
        User user = new User("user1", "Alice");
        String cocktailID = "cocktail1";

        FakeFavouriteDataAccessObject dataAccess =
                new FakeFavouriteDataAccessObject(true);
        FakeFavouritePresenter presenter = new FakeFavouritePresenter();

        FavouriteInteractor interactor =
                new FavouriteInteractor(dataAccess, presenter);

        FavouriteInputData inputData =
                new FavouriteInputData(user, cocktailID);

        interactor.execute(inputData);

        assertTrue(user.isFavourite(cocktailID));
        assertTrue(dataAccess.saveCalled);
        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);
        assertTrue(presenter.outputData.getIsFavourite());
    }

    @Test
    void cocktailDoesNotExistTest() {
        User user = new User("user1", "Alice");
        String cocktailID = "invalidCocktail";

        FakeFavouriteDataAccessObject dataAccess =
                new FakeFavouriteDataAccessObject(false);
        FakeFavouritePresenter presenter = new FakeFavouritePresenter();

        FavouriteInteractor interactor =
                new FavouriteInteractor(dataAccess, presenter);

        FavouriteInputData inputData =
                new FavouriteInputData(user, cocktailID);

        interactor.execute(inputData);

        assertFalse(user.isFavourite(cocktailID));
        assertFalse(dataAccess.saveCalled);
        assertFalse(presenter.successCalled);
        assertTrue(presenter.failCalled);
        assertEquals(
                "invalidCocktail: Cocktail does not exist.",
                presenter.errorMessage
        );
    }

    @Test
    void unfavouriteTest() {
        User user = new User("user1", "Alice");
        String cocktailID = "cocktail1";

        user.favourite(cocktailID);

        FakeFavouriteDataAccessObject dataAccess =
                new FakeFavouriteDataAccessObject(true);
        FakeFavouritePresenter presenter = new FakeFavouritePresenter();

        FavouriteInteractor interactor =
                new FavouriteInteractor(dataAccess, presenter);

        FavouriteInputData inputData =
                new FavouriteInputData(user, cocktailID);

        interactor.execute(inputData);

        assertFalse(user.isFavourite(cocktailID));
        assertTrue(dataAccess.saveCalled);
        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);
        assertFalse(presenter.outputData.getIsFavourite());
    }

    private static class FakeFavouriteDataAccessObject
            implements FavouriteDataAccessInterface {

        private final boolean cocktailExists;
        private boolean saveCalled = false;

        FakeFavouriteDataAccessObject(boolean cocktailExists) {
            this.cocktailExists = cocktailExists;
        }

        @Override
        public void save(User user) {
            saveCalled = true;
        }

        @Override
        public User get(String userId) {
            return null;
        }

        @Override
        public boolean existsByID(String cocktailId) {
            return cocktailExists;
        }
    }

    private static class FakeFavouritePresenter
            implements FavouriteOutputBoundary {

        private boolean successCalled = false;
        private boolean failCalled = false;
        private FavouriteOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(FavouriteOutputData outputData) {
            successCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            this.errorMessage = errorMessage;
        }
    }
}
