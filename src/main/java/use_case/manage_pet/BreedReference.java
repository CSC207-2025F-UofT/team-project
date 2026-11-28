package use_case.manage_pet;

/** General information about a cat breed. */
public final class BreedReference {
    private final String name;
    private final String temperament;
    private final byte[] photo;

    /**
     * Creates a breed reference from the provider's response.
     * @param name the breed's name
     * @param temperament the breed's typical temperament
     * @param photo an encoded reference photo, or an empty array when unavailable
     */
    public BreedReference(String name, String temperament, byte[] photo) {
        this.name = name;
        this.temperament = temperament;
        this.photo = photo.clone();
    }

    /** @return the breed's name */
    public String getName() {
        return name;
    }

    /** @return the description of the breed's typical temperament */
    public String getTemperament() {
        return temperament;
    }

    /** @return the encoded reference photo, or an empty array when unavailable */
    public byte[] getPhoto() {
        return photo.clone();
    }
}
