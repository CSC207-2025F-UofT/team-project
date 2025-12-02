package interface_adapter.pet_management;

import interface_adapter.ViewModel;

public class PetManagementViewModel extends ViewModel<PetManagementState> {

    public PetManagementViewModel() {
        super("petManagement");
        setState(new PetManagementState());
    }

    public void setStatusMessage(String message) {
        PetManagementState state = getState();
        state.setStatusMessage(message);
        firePropertyChange();
    }
}
