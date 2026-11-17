package Entities;

public class UserProfile {
    private String firstName = null;
    private String lastName = null;
    private Integer age = null;
    private String gender = null;
    private String occupation = null;
    private String biography = null;

    public UserProfile(String firstName, String lastName, int age, String gender, String occupation, 
    String biography) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.occupation = occupation;
        this.biography = biography;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public static UserProfile UserProfileFactory(String firstName, String lastName, Integer age, String gender, 
    String occupation, String biography) {
        return new UserProfile(firstName, lastName, age, gender, occupation, biography);
    }
}