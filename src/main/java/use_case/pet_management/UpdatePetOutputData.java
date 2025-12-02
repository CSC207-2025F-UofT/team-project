package use_case.pet_management;

public class UpdatePetOutputData {
    private final boolean success;
    private final String message;

    public UpdatePetOutputData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }

    public String getMessage() { return message; }
}
