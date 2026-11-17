package BoundaryData;

/**
 * Represents the output data returned by a use case through an application boundary.
 * Includes response code for the data packet (default 200).
 */
public class OutputData {
    private int code;

    public OutputData(int code) {
        this.code = code;
    }

    public OutputData() {
        this(200);
    }

    /**
     * @return the response code of this data packet
     */
    public int getCode() {
        return this.code;
    }

    /**
     * @param code set the response code of this data packet
     */
    public void setCode(int code) {
        this.code = code;
    }
}
