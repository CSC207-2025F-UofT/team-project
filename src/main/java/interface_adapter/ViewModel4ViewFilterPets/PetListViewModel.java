
package interface_adapter.ViewModel4ViewFilterPets;

import entity.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetListViewModel {

    private List<Pet> pets = new ArrayList<>();
    private String errorMessage;

    public List<Pet> getPets() {
        return pets;
    }
    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

