package entity;


public class Category {

    private String name;
    private double monthlyGoal; // optional, 0 if not set

    public Category(String name, String description, double monthlyGoal) {
        this.name = name;
        this.monthlyGoal = monthlyGoal;
    }

    public String getName() {
        return name;
    }

    public double getMonthlyGoal() {
        return monthlyGoal;
    }

    public void rename(String newName) {
        this.name = newName;
    }

    public void updateMonthlyGoal(double newGoalAmount) {
        if (newGoalAmount < 0) {
            throw new IllegalArgumentException("Monthly goal cannot be negative.");
        }
        this.monthlyGoal = newGoalAmount;
    }
}
