package app;

import interface_adapter.filter_search.FilterSearchController;
import interface_adapter.filter_search.FilterSearchViewModel;
import interface_adapter.filter_search.FilterSearchState;

import java.time.LocalDate;
import java.util.Scanner;

public class FilterSearchConsole {

    private final FilterSearchController controller;
    private final FilterSearchViewModel viewModel;
    private final Scanner scanner = new Scanner(System.in);

    public FilterSearchConsole(FilterSearchController controller,
                               FilterSearchViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
    }

    public void run() {

        System.out.println("\n===== FILTER SEARCH =====");

        System.out.print("Search text: ");
        String text = scanner.nextLine();

        System.out.print("Category (or empty): ");
        String category = scanner.nextLine();
        if (category.isEmpty()) category = null;

        System.out.print("Start date (yyyy-MM-dd or empty): ");
        String startInput = scanner.nextLine();
        LocalDate startDate = startInput.isEmpty() ? null : LocalDate.parse(startInput);

        System.out.print("End date (yyyy-MM-dd or empty): ");
        String endInput = scanner.nextLine();
        LocalDate endDate = endInput.isEmpty() ? null : LocalDate.parse(endInput);

        System.out.print("Min amount (or empty): ");
        String minInput = scanner.nextLine();
        Double minAmount = minInput.isEmpty() ? null : Double.parseDouble(minInput);

        System.out.print("Max amount (or empty): ");
        String maxInput = scanner.nextLine();
        Double maxAmount = maxInput.isEmpty() ? null : Double.parseDouble(maxInput);

        controller.execute(text, category, startDate, endDate, minAmount, maxAmount);

        render();
    }

    private void render() {
        FilterSearchState state = viewModel.getState();

        if (state.getErrorMessage() != null) {
            System.out.println("Error: " + state.getErrorMessage());
            return;
        }

        if (state.getResults() == null || state.getResults().isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        System.out.println("\nSearch Results:");
        state.getResults().forEach(tx -> {
            System.out.printf("%s | %s | %.2f | %s\n",
                    tx.getDate(), tx.getCategory(), tx.getAmount(), tx.getDescription());
        });
    }
}
