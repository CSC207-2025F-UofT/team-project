package interface_adapter.communicate_request;

import interface_adapter.ViewModel;

/**
 * View model for the child communication board.
 */
public class CommunicateRequestViewModel
        extends ViewModel<CommunicateRequestState> {

    public static final String VIEW_NAME = "main";

    public CommunicateRequestViewModel() {
        super(VIEW_NAME);
        setState(new CommunicateRequestState());
    }
}
