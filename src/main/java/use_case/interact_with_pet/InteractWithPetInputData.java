package use_case.interact_with_pet;

public class InteractWithPetInputData {

    private final String username;

    public InteractWithPetInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}