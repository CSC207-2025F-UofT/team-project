package use_case.pet_management;

public class DeletePetOutputData {
    private final boolean success;
    private final String message;

    public DeletePetOutputData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }

    public String getMessage() { return message; }
}
