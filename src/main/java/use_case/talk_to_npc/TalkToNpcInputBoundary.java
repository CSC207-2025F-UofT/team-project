package use_case.talk_to_npc;

/**
 * What the player can do in a conversation. Called by the
 * TalkToNpcController and implemented by the TalkToNpcInteractor.
 *
 * A conversation has three steps, so there are three methods:
 * walk up to someone, say something back, and walk away.
 */
public interface TalkToNpcInputBoundary {

    /** The player starts talking to a character. */
    void start(StartTalkInputData inputData);

    /** The player picks one of the responses on screen. */
    void choose(ChooseOptionInputData inputData);

    /** The player is done reading and goes back to the game screen. */
    void leave();
}
