package interface_adapter.meal_planning;

import use_case.meal_planning.AddPlannedMealOutputBoundary;
import use_case.meal_planning.AddPlannedMealOutputData;

import java.util.Locale;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import use_case.meal_planning.AddPlannedMealOption;
import use_case.meal_planning.AddPlannedMealOptionsOutputData;

public class AddPlannedMealPresenter
        implements AddPlannedMealOutputBoundary {

    private final AddPlannedMealViewModel viewModel;

    public AddPlannedMealPresenter(
            AddPlannedMealViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel);
    }

    @Override
    public void prepareOptionsView(
            AddPlannedMealOptionsOutputData outputData) {
        List<AddPlannedMealViewModel.OptionItem> slotItems =
                new ArrayList<>();
        for (AddPlannedMealOption option :
                outputData.getMealSlots()) {
            slotItems.add(new AddPlannedMealViewModel.OptionItem(
                    option.getId(), option.getName()));
        }

        List<AddPlannedMealViewModel.OptionItem> foodItems =
                new ArrayList<>();
        for (AddPlannedMealOption option :
                outputData.getFoods()) {
            foodItems.add(new AddPlannedMealViewModel.OptionItem(
                    option.getId(), option.getName()));
        }

        viewModel.updateOptions(slotItems, foodItems);
    }

    @Override
    public void prepareSuccessView(
            AddPlannedMealOutputData outputData) {
        String message = String.format(
                Locale.US,
                "Food added. Meal total: %.1f kcal, %.1f g protein.",
                outputData.getCalories(),
                outputData.getProtein());

        viewModel.update(message, true);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.update(errorMessage, false);
    }
}