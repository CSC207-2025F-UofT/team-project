package use_case.view_pet_details;

import entity.Pet;

/**
 * The cat to show on the detail page.
 */
public class ViewPetDetailsOutputData {
    private final Pet pet;

    /**
     * Creates the result.
     *
     * @param pet the cat found
     */
    public ViewPetDetailsOutputData(Pet pet) {
        this.pet = pet;
    }

    /**
     * Returns the cat.
     *
     * @return the cat found
     */
    public Pet getPet() {
        return pet;
    }
}
