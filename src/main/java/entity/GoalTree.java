package entity;
import java.time.YearMonth;

public class GoalTree {
    private String status;
    private Goal goal;
    private int x_coordinate;
    private int y_coordinate;

    public GoalTree(Goal goal, int x, int y) {
        this.goal = goal;
        this.x_coordinate = x;
        this.y_coordinate = y;
        this.status = "sapling";
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Goal getGoal() {
        return goal;
    }
    // we don't want to change goal so it will not have a setter


    public int getX_coordinate() {
        return x_coordinate;
    }

    public int getY_coordinate() {
        return y_coordinate;
    }

    public void set_coordinates(int x_coordinate, int  y_coordinate) {
        this.x_coordinate = x_coordinate;
        this.y_coordinate = y_coordinate;
    }

    public void update_status(){

        YearMonth currentMonth = YearMonth.now();
        YearMonth goalMonth = goal.getMonth();
        int spent = 0;

        for (Category c: goal.getCategories){
            spent += 0;
        }
        // this loop will be updated based on later implementations


        if (currentMonth.equals(goalMonth)) {
            status = "sapling";
            return;
        }

        if (spent > goal.getGoalAmount()) {
            status = "rotten";
        } else {
            status = "healthy";
        }
    }
}
