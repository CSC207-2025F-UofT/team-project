
package app;

import data_access.InMemoryFoodDataAccessObject;
import interface_adapter.add_food.AddFoodController;
import interface_adapter.add_food.AddFoodPresenter;
import interface_adapter.add_food.AddFoodViewModel;
import use_case.add_food.AddFoodInputBoundary;
import use_case.add_food.AddFoodInteractor;
import use_case.add_food.AddFoodOutputBoundary;
import view.AddFoodView;

/**
 * Builds and connects the components of the NutriTrack application.
 *
 * AppBuilder is responsible for dependency injection, ensuring
 * that use cases depend on interfaces rather than concrete
 * implementations.
 *
 * The current implementation uses in-memory food storage.
 */
public class AppBuilder {

    // Shared food repository for application use cases.
    private final InMemoryFoodDataAccessObject foodDataAccess;

    /**
     * Constructs the application builder and initializes storage.
     */
    public AppBuilder() {
        this.foodDataAccess = new InMemoryFoodDataAccessObject();
    }

    /**
     * Builds the Add Food feature and connects its dependencies.
     *
     * @return configured Add Food Swing view
     */
    public AddFoodView buildAddFoodView() {

        // Create a view model to store UI-facing results.
        AddFoodViewModel viewModel = new AddFoodViewModel();

        // Create the presenter implementing the output boundary.
        AddFoodOutputBoundary presenter =
                new AddFoodPresenter(viewModel);

        // Create the interactor using the shared repository.
        AddFoodInputBoundary interactor =
                new AddFoodInteractor(
                        foodDataAccess,
                        presenter
                );

        // Create the controller using the input boundary.
        AddFoodController controller =
                new AddFoodController(interactor);

        // Connect the controller and view model to the Swing view.
        return new AddFoodView(controller, viewModel);
    }
}
