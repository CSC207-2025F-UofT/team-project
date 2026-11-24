package Entities;

public class UserProfile {
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String occupation;
    private String biography;
    private String contactUrl;
    private String profilePictureUrl;

    private UserProfile(String firstName, String lastName, int age, String gender, String occupation, 
    String biography, String contactUrl, String profilePictureUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.occupation = occupation;
        this.biography = biography;
        this.contactUrl = contactUrl;
        this.profilePictureUrl = profilePictureUrl;
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

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public static UserProfile UserProfileFactory(String firstName, String lastName, Integer age, String gender,
                                                 String occupation, String biography, String contactUrl,
                                                 String profilePictureUrl) {
        return new UserProfile(firstName, lastName, age, gender, occupation, biography, contactUrl, profilePictureUrl);
    }
}