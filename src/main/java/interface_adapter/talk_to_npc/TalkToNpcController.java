package interface_adapter.talk_to_npc;

import use_case.talk_to_npc.ChooseOptionInputData;
import use_case.talk_to_npc.StartTalkInputData;
import use_case.talk_to_npc.TalkToNpcInputBoundary;

/**
 * Turns button presses on the game and dialogue screens into calls on
 * the talk use case.
 */
public class TalkToNpcController {

    private final TalkToNpcInputBoundary talkToNpcInteractor;

    public TalkToNpcController(TalkToNpcInputBoundary talkToNpcInteractor) {
        this.talkToNpcInteractor = talkToNpcInteractor;
    }

    /** The player pressed a Talk button. */
    public void start(String npcId) {
        talkToNpcInteractor.start(new StartTalkInputData(npcId));
    }

    /** The player pressed one of the response buttons. */
    public void choose(String npcId, String nodeId, String optionId) {
        talkToNpcInteractor.choose(new ChooseOptionInputData(npcId, nodeId, optionId));
    }

    /** The player pressed Back after reading what happened. */
    public void leave() {
        talkToNpcInteractor.leave();
    }
}
