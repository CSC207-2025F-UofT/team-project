package interface_adapter.talk_to_npc;

import interface_adapter.ViewModel;

/**
 * The view model behind the dialogue screen. The TalkToNpcPresenter
 * changes its state and the DialogueView listens for the changes.
 *
 * It fires two kinds of property change:
 *   "state" - the conversation changed, so redraw the screen;
 *   "error" - something went wrong, so show the error message.
 */
public class DialogueViewModel extends ViewModel<DialogueState> {

    public static final String VIEW_NAME = "dialogue";
    public static final String ERROR_PROPERTY = "error";

    public DialogueViewModel() {
        super(VIEW_NAME);
        setState(new DialogueState());
    }
}
