package entity;

/** Source details for a read-only listing published by another organization. */
public final class ExternalPetListing {
    private final String organizationName;
    private final String organizationUrl;
    private final String adoptionUrl;
    private final String ageLabel;
    private final boolean ageKnown;

    /**
     * Describes where an external listing came from and what age information was provided.
     * @param organizationName the rescue responsible for the listing
     * @param organizationUrl the rescue's website, or an empty string
     * @param adoptionUrl where to learn how to adopt, or an empty string
     * @param ageLabel the reported age or age category
     * @param ageKnown whether the age in years is known
     */
    public ExternalPetListing(String organizationName, String organizationUrl, String adoptionUrl,
                              String ageLabel, boolean ageKnown) {
        this.organizationName = organizationName;
        this.organizationUrl = organizationUrl;
        this.adoptionUrl = adoptionUrl;
        this.ageLabel = ageLabel;
        this.ageKnown = ageKnown;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getOrganizationUrl() {
        return organizationUrl;
    }

    public String getAdoptionUrl() {
        return adoptionUrl;
    }

    public String getAgeLabel() {
        return ageLabel;
    }

    /** @return whether the listing reports enough information for age filters */
    public boolean hasKnownAge() {
        return ageKnown;
    }
}
