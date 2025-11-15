package game.data_access;

import game.use_case.GetPetFact.PetFactDataAccessInterface;
import game.use_case.GetPetFact.GetPetFactOutputData;

import java.util.Optional;

public class DogApiPetFactDataAccessObject
        implements game.use_case.GetPetFact.PetFactDataAccessInterface {

    @Override
    public Optional<GetPetFactOutputData> fetchFact(String species, String breed) {
        // TODO: Create API call
        return Optional.empty();
    }
}
