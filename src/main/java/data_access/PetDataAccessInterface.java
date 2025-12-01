package data_access;

import entity.Pet;
import java.util.List;

public interface PetDataAccessInterface {
    List<Pet> getPets() throws Exception;
}

