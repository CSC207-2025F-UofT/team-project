package use_case.pet_management;

import entity.Pet;
import java.util.List;

public interface PetDataAccessInterface {

    boolean existsById(int id);

    Pet getById(int id);

    List<Pet> getAll();

    void save(Pet pet);      // add or update

    void deleteById(int id);
}
