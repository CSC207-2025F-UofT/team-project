package interface_adapter.checkOutfit;

import interface_adapter.ViewModel;

public class CheckOutfitViewModel extends ViewModel<CheckOutfitState> {

    public CheckOutfitViewModel() {
        super("Checkout Fit View");
        setState(new CheckOutfitState());
    }
}
