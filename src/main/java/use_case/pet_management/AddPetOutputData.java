package use_case.pet_management;

public class AddPetOutputData {
    private final boolean success;
    private final String message;

    public AddPetOutputData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }

    public String getMessage() { return message; }
}
