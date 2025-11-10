package entity;


public class Category {

    private String name;

    public Category(String name, String description) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void rename(String newName) {
        this.name = newName;
    }

}
