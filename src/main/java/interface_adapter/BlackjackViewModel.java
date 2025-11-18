
package interface_adapter;

import entity.*;

public class BlackjackViewModel {
    public void updateHands(Hand player, Hand dealer) {
        System.out.println("Player: " + player.getValue() + ", Dealer: " + dealer.getValue());
    }

    public void updateWallet(double wallet) {
        System.out.println("Wallet: $" + wallet);
    }

    public void showResult(GameResult result) {
        System.out.println("Result: " + result);
    }
}