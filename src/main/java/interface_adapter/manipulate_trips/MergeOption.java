package interface_adapter.manipulate_trips;

/** Another trip the user can merge the current one with. */
public class MergeOption {
    private final String tripId;
    private final String label;

    public MergeOption(String tripId, String label) {
        this.tripId = tripId;
        this.label = label;
    }

    public String getTripId() { return tripId; }
    /** "Tokyo (12–17 Nov 2025)". */
    public String getLabel() { return label; }

    /** Swing combo boxes and JOptionPane pickers display toString(). */
    @Override
    public String toString() { return label; }
}