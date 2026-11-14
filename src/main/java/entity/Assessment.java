package entity;

/**
 * A graded piece of work in a course, such as a test, an assignment or an exam.
 */
public class Assessment {

    private static final double MAX_PERCENTAGE = 100;

    private final String name;
    private final double mark;
    private final double weight;

    /**
     * Creates a new assessment.
     * @param name the non-empty name of the assessment
     * @param mark the mark earned, as a percentage from 0 to 100
     * @param weight how much of the course grade the assessment is worth,
     *               as a percentage greater than 0 and at most 100
     * @throws IllegalArgumentException if the name is empty, or the mark or weight is out of range
     */
    public Assessment(String name, double mark, double weight) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Assessment name cannot be empty");
        }
        if (Double.isNaN(mark) || mark < 0 || mark > MAX_PERCENTAGE) {
            throw new IllegalArgumentException("Mark must be between 0 and 100");
        }
        if (Double.isNaN(weight) || weight <= 0 || weight > MAX_PERCENTAGE) {
            throw new IllegalArgumentException("Weight must be greater than 0 and at most 100");
        }
        this.name = name.trim();
        this.mark = mark;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getMark() {
        return mark;
    }

    public double getWeight() {
        return weight;
    }
}
