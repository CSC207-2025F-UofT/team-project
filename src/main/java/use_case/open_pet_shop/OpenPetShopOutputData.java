package use_case.open_pet_shop;

public class OpenPetShopOutputData {
    private final String username;
    private final int coinBalance;
    private final int foodQuantity;
    private final int medicineQuantity;

    public OpenPetShopOutputData(
            String username,
            int coinBalance,
            int foodQuantity,
            int medicineQuantity) {
        this.username = username;
        this.coinBalance = coinBalance;
        this.foodQuantity = foodQuantity;
        this.medicineQuantity = medicineQuantity;
    }

    public String getUsername() {
        return username;
    }

    public int getCoinBalance() {
        return coinBalance;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public int getMedicineQuantity() {
        return medicineQuantity;
    }
}