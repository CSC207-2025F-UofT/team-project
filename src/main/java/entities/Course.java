package entities;

public class Course {
    private String id;
    private String code;  // Course code
    private String name;  // Course name
    private String colourHex;  // Colour to appear in calendar

    public Course(String id, String code, String name, String colourHex) {
        if (colourHex == null || colourHex.matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("Colour should be in Hexcode (#RRGGBB)");
        }

        this.id = id;
        this.code = code;
        this.name = name;
        this.colourHex = colourHex;
    }

}
