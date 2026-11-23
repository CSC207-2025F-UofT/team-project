package interface_adapter.browse_pets;

/**
 * Text for one pet card, already formatted for display.
 */
public class PetCardData {
    private final String petId;
    private final String name;
    private final String summary;
    private final String status;

    /**
     * Creates the card text.
     *
     * @param petId the pet's id, used to open its detail page
     * @param name the pet's name
     * @param summary a short line such as "Cat, Tabby, 2 years old"
     * @param status the adoption status written out in words
     */
    public PetCardData(String petId, String name, String summary, String status) {
        this.petId = petId;
        this.name = name;
        this.summary = summary;
        this.status = status;
    }

    /**
     * Returns the pet's id.
     *
     * @return the id
     */
    public String getPetId() {
        return petId;
    }

    /**
     * Returns the pet's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the short description line.
     *
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Returns the status in words.
     *
     * @return the status text
     */
    public String getStatus() {
        return status;
    }
}
