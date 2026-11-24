package entities;
// TODO: figure out how to handle IllegalArgumentationException on user side
// want to let user know of the error but able to try again
/*
  Object for Events that carry a grade.
  Changes have to made through Event, but can get info this.
*/
public class GradeInfo {
    private double weight;  // percent of course grade
    private double total;  // max marks
    private Double earned;  // Achieved mark. null if not marked yet

    public GradeInfo(Double weight, Double total, Double earned) {
        if (weight < 0 || weight > 100) {
            throw new IllegalArgumentException("Weight must be a value from 0 - 100");
        }
        if (total <= 0 ) {
            throw new IllegalArgumentException("total mark must be positive");
        }
        if (earned != null && earned < 0) {
            throw new IllegalArgumentException("Earned mark must be positive");
        }

        this.weight = weight;
        this.total = total;
        this.earned = earned;
    }

    // Getters
    public double getWeight() { return this.weight; }
    public double getTotal() { return this.total; }
    // TODO: Decide what to do if not marked yet.
    public Double getEarned() {
        if (!isMarked()) {
            throw new IllegalArgumentException("Has no mark");
        }
        return this.earned;
    }
    public boolean isMarked() { return this.earned != null; }

    // Setters
    // Package private, accessed by Event
    void setWeight(double newWeight) {
        if (weight < 0 || weight > 100) {
            throw new IllegalArgumentException("Weight must be a value from 0 - 100");
        }
        this.weight = newWeight;
    }
    void setTotal(double newTotal) {
        if (total <= 0 ) {
            throw new IllegalArgumentException("total mark must be positive");
        }
        this.total = newTotal;
    }
    void setEarned(Double newEarned) {
        if (earned != null && earned < 0) {
            throw new IllegalArgumentException("Earned mark must be positive");
        }
        this.earned = newEarned;
    }


    // Methods
    public double getPercent() {
        if (this.earned == null) {
            throw new IllegalArgumentException("No earned mark");
        }
        return (this.earned / this.total * 100);
    }

}
