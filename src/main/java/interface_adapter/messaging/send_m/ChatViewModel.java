package interface_adapter.messaging.send_m;

import interface_adapter.ViewModel;

/**
 * ViewModel for the ChatView.
 */
public class ChatViewModel extends ViewModel<ChatState> {

    public ChatViewModel() {
        super("chat");
        this.setState(new ChatState());
    }
}
