package entity;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class Goal {

    private YearMonth month;
    private List<Category> categories;
    private float goalAmount;


    public Goal(YearMonth month, float goalAmount) {
        this.month = month;
        this.categories = new ArrayList<>(categories);
        this.goalAmount = goalAmount;
    }

    // Alternative constructor for utility
    public Goal(YearMonth month, List<Category> categories, float goalAmount) {
        this.month = month;
        this.categories = categories;
        this.goalAmount = goalAmount;
    }

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public boolean removeCategory(Category category) {

        if (categories.contains(category)) {
            categories.remove(category);
            return true;
        }

        return false; // return if the removal was successful or not
    }

    public float getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(float goalAmount) {
        this.goalAmount = goalAmount;
    }

}


