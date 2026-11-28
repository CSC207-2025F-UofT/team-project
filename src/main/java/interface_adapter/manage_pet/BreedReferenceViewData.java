package interface_adapter.manage_pet;

/** Breed suggestions displayed while editing a pet. */
public final class BreedReferenceViewData {
    private final String name;
    private final String temperament;
    private final byte[] photo;

    BreedReferenceViewData(String name, String temperament, byte[] photo) {
        this.name = name;
        this.temperament = temperament;
        this.photo = photo;
    }

    /** @return the breed being described */
    public String getName() {
        return name;
    }

    /** @return the typical traits for the breed */
    public String getTemperament() {
        return temperament;
    }

    /** @return the optional reference photo, separate from the listing photo */
    public byte[] getPhoto() {
        return photo.clone();
    }
}
