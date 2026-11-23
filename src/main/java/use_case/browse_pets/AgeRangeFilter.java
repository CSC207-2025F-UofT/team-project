package use_case.browse_pets;

import entity.Pet;

/**
 * Keeps only pets whose age in years falls inside a range. Both ends are included.
 */
public class AgeRangeFilter implements PetFilter {
    private final int minYears;
    private final int maxYears;

    /**
     * Creates a filter for an age range.
     *
     * @param minYears the youngest age to keep, 0 or more
     * @param maxYears the oldest age to keep, not less than minYears
     * @throws IllegalArgumentException if the range is negative or backwards
     */
    public AgeRangeFilter(int minYears, int maxYears) {
        if (minYears < 0 || maxYears < minYears) {
            throw new IllegalArgumentException("Age range must be 0 or more and min cannot exceed max.");
        }
        this.minYears = minYears;
        this.maxYears = maxYears;
    }

    @Override
    public boolean matches(Pet pet) {
        return pet.getAgeYears() >= minYears && pet.getAgeYears() <= maxYears;
    }
}
