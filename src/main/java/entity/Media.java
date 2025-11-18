package entity;

public abstract class Media {
    public String name;
    private final int referenceNumber;
    public int[] genreIDs;

    public Media(String name, int referenceNumber, int[] genreIDs) {
        this.name = name;
        this.referenceNumber = referenceNumber;
        this.genreIDs = genreIDs;
    }

    public int getReferenceNumber() {
        return referenceNumber;
    }
}
