package entity;

/** The optional coat and microchip information shown on a pet's profile. */
public final class PetProfileDetails {
    /** Defaults for listings saved before these fields were added. */
    public static final PetProfileDetails UNLISTED = new PetProfileDetails("", "", null);

    private final String color;
    private final String hairLength;
    private final Boolean microchipped;

    /** Trims the coat details. A null microchip status means it was unlisted. */
    public PetProfileDetails(String color, String hairLength, Boolean microchipped) {
        this.color = color.strip();
        this.hairLength = hairLength.strip();
        this.microchipped = microchipped;
    }

    public String getColor() {
        return color;
    }

    public String getHairLength() {
        return hairLength;
    }

    public Boolean getMicrochipped() {
        return microchipped;
    }
}
