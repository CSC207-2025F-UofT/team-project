package use_case.talk_to_npc;

/**
 * Implemented by the TalkToNpcPresenter. The interactor calls one of
 * these depending on what happened.
 */
public interface TalkToNpcOutputBoundary {

    /** A conversation started: show what the character says and the responses. */
    void prepareDialogueView(StartTalkOutputData outputData);

    /** The player picked a response: show what happened and the new stats. */
    void prepareResultView(ChooseOptionOutputData outputData);

    /** Something went wrong (for example, there is no game to play). */
    void prepareFailView(String errorMessage);

    /** The conversation is over: go back to the game screen. */
    void prepareGameView();
}
