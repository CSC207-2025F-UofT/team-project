package entities;

/*
  Object for Events that carry a grade.
*/
public class GradeInfo {
    private double weight;  // percent of course grade
    private double possible;  // max marks
    private Double earned;  // Achieved mark. null if not marked yet

    public GradeInfo(Double weight, Double possible, Double earned) {
        if (weight < 0 || weight > 100) {
            throw new IllegalArgumentException("Weight must be a value from 0 - 100");
        }
        if (possible <= 0 ) {
            throw new IllegalArgumentException("Possible mark must be positive");
        }
        if (earned != null && earned < 0) {
            throw new IllegalArgumentException("Earned mark must be positive");
        }

        this.weight = weight;
        this.possible = possible;
        this.earned = earned;
    }

    // Getters
    public double getWeight() { return this.weight; }
    public double getPossible() { return this.possible; }
    // TODO: Decide what to do if not marked yet.
    public Double getEarned() {
        if (!isMarked()) {
            throw new IllegalArgumentException("Has no mark");
        }
        return this.earned;
    }
    public boolean isMarked() { return this.earned != null; }


    // Methods

}
