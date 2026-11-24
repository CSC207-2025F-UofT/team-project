package entities;
import java.util.*;

/**
 * An entity representing a user's cognitive data.
 */
public class CognitiveData {
    private Date date;
    private String mood;
    private Object[] cogTest;
    private Double productivityRating;

    /**
     * Creates a CognitiveData using a CogBuilder.
     * @param b the builder used to create the object.
     */
    private CognitiveData(CogBuilder b) {
        this.date = b.date;
        this.mood = b.mood;
        this.cogTest = b.cogTest;
        this.productivityRating = b.productivityRating;
    }

    /**
     *  CognitiveData class builder (follows Creational Builder design pattern)
     */
    public static class CogBuilder {
        private Date date;
        private String mood = "none";
        private Object[] cogTest = {"blank", 0.0};
        private Double productivityRating = 0.0;

        public CogBuilder(Date date) {
            this.date = date;
        }

        public CogBuilder mood(String mood) {
            this.mood = mood;
            return this;
        }

        public CogBuilder cogTest(String cogTestType, Double cogTestResult) {
            this.cogTest = new Object[]{cogTestType, cogTestResult};
            return this;
        }

        public CogBuilder productivityRating(Double productivityRating) {
            this.productivityRating = productivityRating;
            return this;
        }

        public CognitiveData build() {
            return new CognitiveData(this);
        }

    }

    /**
     * @return the date
     */
    public Date getDate() { return this.date; }

    /**
     * @return the productivity rating
     */
    public double getProductivity() { return this.productivityRating; }

    /**
     * @return the cognitive test (returns an array of: {cognitive test type, cognitive test score})
     */
    public Object[] getCognitiveTest() { return this.cogTest; }

    /**
     * @return the mood
     */
    public String getMood() { return this.mood; }


    /**
     * Returns a Hashtable, with nested arrays, representing this CognitiveData of the format:
     * {date : [mood, cogTest, productivityRating]}
     */
    public Hashtable<Date, Object[]> getCognitiveResults() {
        Hashtable<Date, Object[]> dict = new Hashtable<Date, Object[]>();
        Object[] list = {this.mood, this.cogTest, this.productivityRating};
        dict.put(this.date, list);
        return dict;
    }

}
