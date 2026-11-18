package entity;

public class AnswerCity extends City {
    private final String landmark;

    public AnswerCity(String name, String landmark, double lat, double lon) {
        super(name, lat, lon);
        this.landmark = landmark;
    }

    public String getLandmark() {
        return this.landmark;
    }
}
