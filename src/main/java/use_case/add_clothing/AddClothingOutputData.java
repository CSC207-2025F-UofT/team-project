package use_case.add_clothing;

/**
 * The result of a successful "add clothing" request, passed from AddClothingInteractor to AddClothingPresenter.
 * Only created when the save works
 */
public class AddClothingOutputData {
    private final String itemId;
    private final String itemName;
    private final String imagePath;

    public AddClothingOutputData(String itemId, String itemName, String imagePath) {
        this.itemId = itemId; //new id, e.g. "c001
        this.itemName = itemName; //where its picture was saved
        this.imagePath = imagePath; //where its picture was saved
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getImagePath() {
        return imagePath;
    }
}
