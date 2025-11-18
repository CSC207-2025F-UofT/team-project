package interface_adapter;

import use_case.MinesInteractor;

public class MinesController {
    private final MinesInteractor interactor;

    public MinesController(MinesInteractor interactor) {
        this.interactor = interactor;
    }

    public void onReveal(int x, int y) {
        interactor.revealTile(x, y);
    }
}