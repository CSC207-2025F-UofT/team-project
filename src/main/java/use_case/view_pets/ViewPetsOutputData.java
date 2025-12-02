package use_case.view_pets;

import entity.Pet;

import java.util.List;

/**
 *The output data for the View Pets Use Case 1 (Angelica's:))
 * This class stores info that the ViewPetsInteractor
 * sends to the presenter once it has recieved the list of pets.
 */
public class ViewPetsOutputData {
    private final List<Pet> pets;
    private final String errorMsg;

    public ViewPetsOutputData(List<Pet> pets, String errorMsg) {
        this.pets = pets;
        this.errorMsg = errorMsg;
    }
    //the getters !
    public List<Pet> getPets() {
        return pets;
    }
    public String getErrorMsg() {
        return errorMsg;
    }


}
