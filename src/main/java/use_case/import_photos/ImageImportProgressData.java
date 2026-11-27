package use_case.import_photos;

public class ImageImportProgressData {
    private final String imagePath;
    private final boolean success;
    private final int totalImages;

    public ImageImportProgressData(String imagePath, boolean success, int totalImages) {
        this.imagePath = imagePath;
        this.success = success;
        this.totalImages = totalImages;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean getSuccess() {
        return success;
    }

    public int getTotalImages() {
        return totalImages;
    }
}
