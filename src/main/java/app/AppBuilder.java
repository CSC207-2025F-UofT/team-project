package app;

import data_access.InMemoryDeckRepository;
import entity.Deck;

import interface_adapter.add_card.AddCardController;
import interface_adapter.add_card.AddCardPresenter;
import interface_adapter.add_card.AddCardViewModel;
import use_case.add_card.AddCardInputBoundary;
import use_case.add_card.AddCardDataAccessInterface;
import use_case.add_card.AddCardInteractor;

import java.util.List;

public class AppBuilder {
    private final AddCardViewModel viewModel;
    private final AddCardController controller;

    public AppBuilder() {
        AddCardDataAccessInterface repository = new InMemoryDeckRepository(
                List.of(new Deck(1, "Test1", "Kobi owner"))
        );

        viewModel = new AddCardViewModel();

        AddCardPresenter presenter = new AddCardPresenter(viewModel);

        AddCardInteractor interactor = new AddCardInteractor(repository, presenter);

        controller = new AddCardController(interactor);
    }

    public AddCardController getAddCardController() {
        return controller;
    }

    public AddCardViewModel getAddCardViewModel() {
        return viewModel;
    }

}
