package app;

import data_access.InMemoryFoodDataAccessObject;
import data_access.InMemoryMealPlanDataAccessObject;
import interface_adapter.add_food.AddFoodController;
import interface_adapter.add_food.AddFoodPresenter;
import interface_adapter.add_food.AddFoodViewModel;
import interface_adapter.meal_planning.AddPlannedMealController;
import interface_adapter.meal_planning.AddPlannedMealPresenter;
import interface_adapter.meal_planning.AddPlannedMealViewModel;
import interface_adapter.meal_planning.CreatePlanController;
import interface_adapter.meal_planning.CreatePlanPresenter;
import interface_adapter.meal_planning.CreatePlanViewModel;
import interface_adapter.view_food_bank.ViewFoodBankController;
import interface_adapter.view_food_bank.ViewFoodBankPresenter;
import interface_adapter.view_food_bank.ViewFoodBankViewModel;
import use_case.add_food.AddFoodInputBoundary;
import use_case.add_food.AddFoodInteractor;
import use_case.add_food.AddFoodOutputBoundary;
import use_case.meal_planning.AddPlannedMealInputBoundary;
import use_case.meal_planning.AddPlannedMealInteractor;
import use_case.meal_planning.AddPlannedMealOutputBoundary;
import use_case.meal_planning.CreatePlanInputBoundary;
import use_case.meal_planning.CreatePlanInteractor;
import use_case.meal_planning.CreatePlanOutputBoundary;
import use_case.view_food_bank.ViewFoodBankInputBoundary;
import use_case.view_food_bank.ViewFoodBankInteractor;
import use_case.view_food_bank.ViewFoodBankOutputBoundary;
import view.AddFoodView;
import view.AddPlannedMealView;
import view.CreatePlanView;
import view.FoodBankView;

public class AppBuilder {

    private final InMemoryFoodDataAccessObject foodDataAccess;
    private final InMemoryMealPlanDataAccessObject mealPlanDataAccess;

    public AppBuilder() {
        foodDataAccess = new InMemoryFoodDataAccessObject();
        mealPlanDataAccess =
                new InMemoryMealPlanDataAccessObject(foodDataAccess);
    }

    public AddFoodView buildAddFoodView() {
        AddFoodViewModel viewModel = new AddFoodViewModel();
        AddFoodOutputBoundary presenter =
                new AddFoodPresenter(viewModel);
        AddFoodInputBoundary interactor =
                new AddFoodInteractor(foodDataAccess, presenter);
        AddFoodController controller =
                new AddFoodController(interactor);

        return new AddFoodView(controller, viewModel);
    }

    public FoodBankView buildFoodBankView() {
        ViewFoodBankViewModel viewModel =
                new ViewFoodBankViewModel();
        ViewFoodBankOutputBoundary presenter =
                new ViewFoodBankPresenter(viewModel);
        ViewFoodBankInputBoundary interactor =
                new ViewFoodBankInteractor(foodDataAccess, presenter);
        ViewFoodBankController controller =
                new ViewFoodBankController(interactor);

        return new FoodBankView(controller, viewModel);
    }

    public CreatePlanView buildCreatePlanView() {
        CreatePlanViewModel viewModel =
                new CreatePlanViewModel();
        CreatePlanOutputBoundary presenter =
                new CreatePlanPresenter(viewModel);
        CreatePlanInputBoundary interactor =
                new CreatePlanInteractor(
                        mealPlanDataAccess, presenter);
        CreatePlanController controller =
                new CreatePlanController(interactor);

        return new CreatePlanView(controller, viewModel);
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

