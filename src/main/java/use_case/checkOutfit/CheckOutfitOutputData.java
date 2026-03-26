package use_case.checkOutfit;

public class CheckOutfitOutputData {
    private final String popUpMessage;

    public CheckOutfitOutputData(String popUpMessage) {
        this.popUpMessage = popUpMessage;
    }

    public String getPopUpMessage() { return popUpMessage; }
}
