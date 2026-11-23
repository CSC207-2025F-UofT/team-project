package entities;

/**
 * An entity representing a Researcher, extending User
 */
public class Researcher extends User {

    public Researcher(String username) {
        super(username, "Researcher");
    }

    @Override
    public String getRole() { return "Researcher"; }

}
