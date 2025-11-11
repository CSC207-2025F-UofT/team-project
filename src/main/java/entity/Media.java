package entity;

public abstract class Media {
    private final int referenceNumber;
    public int[] genreIDs;

    public Media(int referenceNumber, int[] genreIDs) {
        this.referenceNumber = referenceNumber;
        this.genreIDs = genreIDs;
    }
}
