package interface_adapter.role_select;

import interface_adapter.ViewModel;

/**
 * View model for the first screen, where the user picks "Adopter" or "Admin".
 * It has no state of its own.
 */
public class RoleSelectViewModel extends ViewModel<Void> {
    public static final String VIEW_NAME = "role select";

    public RoleSelectViewModel() {
        super(VIEW_NAME);
    }
}
