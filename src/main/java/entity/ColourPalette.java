package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColourPalette {
    private String name;
    private String baseColour; // hex code
    private String schemeType;
    private final List<String> colours = new ArrayList<>();

    // Creates an empty palette when a new project is created.
    public ColourPalette() {
        this.name = "Untitled Palette";
    }

    public ColourPalette(String name, String baseColour, String schemeType) {
        setName(name);
        this.baseColour = validateHex(baseColour);
        this.schemeType = validateSchemeType(schemeType);
    }

    // Fills the palette once the user picks a base colour and scheme.
    public void generate(String baseColour, String schemeType, List<String> newColours) {
        String validBase = validateHex(baseColour);
        String validScheme = validateSchemeType(schemeType);
        setColours(newColours);
        this.baseColour = validBase;
        this.schemeType = validScheme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public String getBaseColour() {
        return baseColour;
    }

    public String getSchemeType() {
        return schemeType;
    }

    public List<String> getColours() {
        return Collections.unmodifiableList(colours);
    }

    public String getColour(int index) {
        return colours.get(index);
    }

    public void addColour(String colour) {
        colours.add(validateHex(colour));
    }

    public boolean removeColour(String colour) {
        if (colour == null) {
            return false;
        }
        return colours.remove(colour.toUpperCase());
    }

    public void setColours(List<String> newColours) {
        if (newColours == null) {
            throw new IllegalArgumentException("Colour list cannot be null");
        }
        List<String> validated = new ArrayList<>();
        for (String colour : newColours) {
            validated.add(validateHex(colour));
        }
        colours.clear();
        colours.addAll(validated);
    }

    public int size() {
        return colours.size();
    }

    public boolean isEmpty() {
        return colours.isEmpty();
    }

    private static String validateHex(String colour) {
        if (colour == null || !colour.matches("#[0-9A-Fa-f]{6}")) {  // the string represents hex code
            throw new IllegalArgumentException("Colour must be a hex string like #FF0000");
        }
        return colour.toUpperCase();
    }

    private static String validateSchemeType(String schemeType) {
        if (schemeType == null || schemeType.isBlank()) {
            throw new IllegalArgumentException("Scheme type cannot be empty");
        }
        return schemeType;
    }
}