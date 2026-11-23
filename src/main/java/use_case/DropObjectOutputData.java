package use_case;

public class DropObjectOutputData {

    private final boolean dropped;

    public DropObjectOutputData(boolean dropped) {
        this.dropped = dropped;
    }

    public boolean wasDropped() {
        return dropped;
    }
}
