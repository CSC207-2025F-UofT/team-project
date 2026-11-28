package data_access;

import java.util.List;

import entity.Pet;
import entity.PetProfileDetails;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;
import entity.Temperament;

/** Supplies the starter listings for a new PawPortal catalog. */
public final class StarterPetCatalog {
    private StarterPetCatalog() {
    }

    /** Returns new listing instances for an empty catalog. */
    public static List<Pet> createPets() {
        final Pet piggy = new Pet("adoptapet-48129404", "Piggy- CP", Species.CAT, "Snowshoe", 8, PetSex.FEMALE,
                List.of(Temperament.AFFECTIONATE), "Piggy can get stressed during travel and vet visits.",
                false, true, true, null, "classpath:/listing-photos/piggy-cp.jpg", PetStatus.AVAILABLE,
                new PetProfileDetails("Brown or Chocolate (Mostly)", "Short", true));
        return List.of(piggy);
    }
}
