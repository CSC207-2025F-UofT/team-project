
package app;

import data_access.InMemoryFoodDataAccessObject;

import interface_adapter.add_food.AddFoodController;
import interface_adapter.add_food.AddFoodPresenter;
import interface_adapter.add_food.AddFoodViewModel;

import interface_adapter.view_food_bank.ViewFoodBankController;
import interface_adapter.view_food_bank.ViewFoodBankPresenter;
import interface_adapter.view_food_bank.ViewFoodBankViewModel;

import use_case.add_food.AddFoodInputBoundary;
import use_case.add_food.AddFoodInteractor;
import use_case.add_food.AddFoodOutputBoundary;

import use_case.view_food_bank.ViewFoodBankInputBoundary;
import use_case.view_food_bank.ViewFoodBankInteractor;
import use_case.view_food_bank.ViewFoodBankOutputBoundary;

import view.AddFoodView;
import view.FoodBankView;

/**
 * Builds and connects the components of the NutriTrack application.
 *
 * AppBuilder is responsible for dependency injection and
 * constructing the application's use cases, interface adapters,
 * and Swing views.
 *
 * All food-related use cases share the same data access object,
 * ensuring that they operate on a consistent food repository.
 *
 * The current implementation uses in-memory storage.
 */
public class AppBuilder {

    /**
     * Shared food repository used by all food-related use cases.
     */
    private final InMemoryFoodDataAccessObject foodDataAccess;

    /**
     * Constructs the application builder.
     *
     * Initializes a single in-memory food repository that will
     * be shared by the Add Food and View Food Bank use cases.
     */
    public AppBuilder() {
        this.foodDataAccess = new InMemoryFoodDataAccessObject();
    }

    /**
     * Builds the Add Food view and its required dependencies.
     *
     * Creates the ViewModel, presenter, interactor, and controller,
     * then injects them into the Swing view.
     *
     * @return configured Add Food view
     */
    public AddFoodView buildAddFoodView() {

        // Create the ViewModel and presenter.
        AddFoodViewModel viewModel = new AddFoodViewModel();

        AddFoodOutputBoundary presenter =
                new AddFoodPresenter(viewModel);

        // Inject the shared food repository into the interactor.
        AddFoodInputBoundary interactor =
                new AddFoodInteractor(
                        foodDataAccess,
                        presenter
                );

        // Connect the controller to the input boundary.
        AddFoodController controller =
                new AddFoodController(interactor);

        // Construct and return the Swing view.
        return new AddFoodView(controller, viewModel);
    }

    /**
     * Builds the View Food Bank view and its required dependencies.
     *
     * Creates the ViewModel, presenter, interactor, and controller,
     * then injects them into the Swing view.
     *
     * Uses the same repository instance as the Add Food use case.
     *
     * @return configured Food Bank view
     */
    public FoodBankView buildFoodBankView() {

        // Create the ViewModel and presenter.
        ViewFoodBankViewModel viewModel =
                new ViewFoodBankViewModel();

        ViewFoodBankOutputBoundary presenter =
                new ViewFoodBankPresenter(viewModel);

        // Inject the same food repository used by Add Food.
        ViewFoodBankInputBoundary interactor =
                new ViewFoodBankInteractor(
                        foodDataAccess,
                        presenter
                );

        // Connect the controller to the input boundary.
        ViewFoodBankController controller =
                new ViewFoodBankController(interactor);

        // Construct and return the Swing view.
        return new FoodBankView(controller, viewModel);
    }
}