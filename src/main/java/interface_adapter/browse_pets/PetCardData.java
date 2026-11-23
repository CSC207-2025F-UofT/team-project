package interface_adapter.browse_pets;

/**
 * Text for one pet card, already formatted for display.
 */
public class PetCardData {
    private final String name;
    private final String summary;
    private final String status;

    /**
     * Creates the card text.
     *
     * @param name the pet's name
     * @param summary a short line such as "Cat, Tabby, 2 years old"
     * @param status the adoption status written out in words
     */
    public PetCardData(String name, String summary, String status) {
        this.name = name;
        this.summary = summary;
        this.status = status;
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
