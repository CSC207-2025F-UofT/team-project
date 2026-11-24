package Entities;

public class Organization {
    private String name;
    private String logoURL = null;
    private Department[] departments;

    private Organization(String name, String logoURL) {
        this.name = name;
        this.logoURL = logoURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public Department[] getDepartments() {
        return departments;
    }

    void setDepartments(Department[] departments) {
        this.departments = departments;
    }

    public static Organization OrganizationFactory(String name, String logoURL) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Organization name is required.");
        }

        return new Organization(name, logoURL);
    }
}