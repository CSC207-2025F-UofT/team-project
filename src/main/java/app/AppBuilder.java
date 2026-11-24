
package app;

import data_access.InMemoryFoodDataAccessObject;
import interface_adapter.add_food.AddFoodController;
import interface_adapter.add_food.AddFoodPresenter;
import interface_adapter.add_food.AddFoodViewModel;
import use_case.add_food.AddFoodInputBoundary;
import use_case.add_food.AddFoodInteractor;
import use_case.add_food.AddFoodOutputBoundary;
import view.AddFoodView;
import view.CreatePlanView;
import use_case.meal_planning.CreatePlanOutputBoundary;
import use_case.meal_planning.CreatePlanInteractor;
import use_case.meal_planning.CreatePlanInputBoundary;
import interface_adapter.meal_planning.CreatePlanController;
import interface_adapter.meal_planning.CreatePlanPresenter;
import interface_adapter.meal_planning.CreatePlanViewModel;
import data_access.InMemoryMealPlanDataAccessObject;
import interface_adapter.meal_planning.AddPlannedMealController;
import interface_adapter.meal_planning.AddPlannedMealPresenter;
import interface_adapter.meal_planning.AddPlannedMealViewModel;
import use_case.meal_planning.AddPlannedMealInputBoundary;
import use_case.meal_planning.AddPlannedMealInteractor;
import use_case.meal_planning.AddPlannedMealOutputBoundary;
import view.AddPlannedMealView;

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
    private final InMemoryMealPlanDataAccessObject mealPlanDataAccess;

    /**
     * Constructs the application builder and initializes storage.
     */
    public AppBuilder() {
        this.foodDataAccess = new InMemoryFoodDataAccessObject();
        this.mealPlanDataAccess =
                new InMemoryMealPlanDataAccessObject(foodDataAccess);;
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

    public CreatePlanView buildCreatePlanView(){
        CreatePlanViewModel viewmodel = new CreatePlanViewModel();
        CreatePlanOutputBoundary presenter = new CreatePlanPresenter(viewmodel);
        CreatePlanInputBoundary interactor = new CreatePlanInteractor(mealPlanDataAccess,
                                                                        presenter);
        CreatePlanController controller = new CreatePlanController(interactor);
        return new CreatePlanView(controller, viewmodel);
    }

    public AddPlannedMealView buildAddPlannedMealView() {
        AddPlannedMealViewModel viewModel =
                new AddPlannedMealViewModel();

        AddPlannedMealOutputBoundary presenter =
                new AddPlannedMealPresenter(viewModel);

        AddPlannedMealInputBoundary interactor =
                new AddPlannedMealInteractor(
                        mealPlanDataAccess, presenter);

        AddPlannedMealController controller =
                new AddPlannedMealController(interactor);

        return new AddPlannedMealView(controller, viewModel);
    }
}
