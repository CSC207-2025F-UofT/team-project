package use_case.drop_object;

public class DropObjectOutputData {

    private final boolean dropped;

    public DropObjectOutputData(boolean dropped) {
        this.dropped = dropped;
    }

    public boolean wasDropped() {
        return dropped;
    }
}
