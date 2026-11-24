package UseCases.SignUp;

import BoundaryData.InputData;

public class SignUpInputData extends InputData{
    private final String username;
    private final String password;
    private final String confirmPassword;
    private final String firstName;
    private final String lastName;
    private final Integer age;
    private final String gender;
    private final String occupation;
    private final String biography;


    public SignUpInputData(String username, String password, String confirmPassword,
        String firstName, String lastName, Integer age, String gender, String occupation,
        String biography) {
        super();
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.occupation = occupation;
        this.biography = biography;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return confirmPassword;
    }

    String getfirstName() {
        return firstName;
    }

    String getlastName() {
        return lastName;
    }

    Integer getAge() {
        return age;
    }

    String getGender() {
        return gender;
    }

    String getOccupation() {
        return occupation;
    }

    String getBiography() {
        return biography;
    }
}
