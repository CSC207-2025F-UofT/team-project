package entity;

public class ProgramTime {
    private java.time.Instant currentTime;
    private static java.time.Duration timeIncrement;

    public ProgramTime(java.time.Instant currentTime) {
        this.currentTime = currentTime;
    }
    public void incrementTime(){
        currentTime = currentTime.plus(timeIncrement);
    }
    public java.time.Instant getCurrentTime() {
        return currentTime;
    }
}
