package use_case.update_profile;

public class UpdateUserProfileInputData {
    private final String name;
    private final String email;
    private final String address;
    private final int phoneNumber;

    public UpdateUserProfileInputData(String name, String email, String address, int phoneNumber) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }
}
