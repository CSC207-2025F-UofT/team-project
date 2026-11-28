package interface_adapter;

import entity.ExternalPetListing;
import entity.Pet;

/** Links and attribution displayed for a pet from an external rescue. */
public final class ExternalListingViewData {
    private final String organizationName;
    private final String organizationUrl;
    private final String adoptionUrl;
    private final String photoUrl;

    /**
     * Prepares the source details for the gallery and detail screen.
     * @param pet the pet from an external rescue
     */
    public ExternalListingViewData(Pet pet) {
        final ExternalPetListing listing = pet.getExternalListing();
        organizationName = listing.getOrganizationName();
        organizationUrl = listing.getOrganizationUrl();
        adoptionUrl = listing.getAdoptionUrl();
        photoUrl = pet.getPhotoPath();
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

    public String getPhotoUrl() {
        return photoUrl;
    }
}
