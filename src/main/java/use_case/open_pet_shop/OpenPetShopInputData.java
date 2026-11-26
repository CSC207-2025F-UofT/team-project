package use_case.open_pet_shop;

public class OpenPetShopInputData {
    private final String username;

    public OpenPetShopInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}