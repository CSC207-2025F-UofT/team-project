
package app;

import view.FoodBankView;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppBuilder appBuilder = new AppBuilder();
            FoodBankView foodBankView =
                    appBuilder.buildFoodBankView();

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Add Food", appBuilder.buildAddFoodView());
            tabs.addTab("Food Bank", foodBankView);
            tabs.addTab("Create Plan", appBuilder.buildCreatePlanView());
            tabs.addTab("Add Planned Meal",
                    appBuilder.buildAddPlannedMealView());

            tabs.addChangeListener(event -> {
                if (tabs.getSelectedComponent() == foodBankView) {
                    foodBankView.refreshFoodBank();
                }
            });

            JFrame frame = new JFrame("NutriTrack");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(tabs);
            frame.setSize(800, 450);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}