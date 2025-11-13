package interface_adapter;

import use_case.BlackjackInteractor;

public class BlackjackController {
    private final BlackjackInteractor interactor;

    public BlackjackController(BlackjackInteractor interactor) {
        this.interactor = interactor;
    }

    public void onDeal(double bet) { interactor.startRound(bet); }
    public void onHit() { interactor.playerHit(); }
    public void onStand() { interactor.stand(); }
}