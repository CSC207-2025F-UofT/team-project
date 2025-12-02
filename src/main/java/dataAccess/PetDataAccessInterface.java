package dataAccess;

import entity.Pet;
import java.util.List;

public interface PetDataAccessInterface {
    List<Pet> getPets() throws Exception;
}