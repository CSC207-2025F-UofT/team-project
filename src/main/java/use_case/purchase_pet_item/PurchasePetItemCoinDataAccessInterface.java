package use_case.purchase_pet_item;

public interface PurchasePetItemCoinDataAccessInterface {
    int getCoinBalance(String userId);

    int spendCoins(String userId, int amount);
}