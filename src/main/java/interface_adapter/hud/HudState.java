package interface_adapter.hud;

/**
 * What the stats panel shows: the day and the four stats.
 * Mutable on purpose (like the login lab's state classes): presenters build
 * a new one or copy and change it, then set it on the HudViewModel.
 */
public class HudState {

    private int day;
    private int totalDays;
    private int energy;
    private int sanity;
    private int academicSurvival;
    private int money;

    public HudState() {
    }

    /** Copy constructor, so presenters can change a copy instead of the live state. */
    public HudState(HudState copy) {
        this.day = copy.day;
        this.totalDays = copy.totalDays;
        this.energy = copy.energy;
        this.sanity = copy.sanity;
        this.academicSurvival = copy.academicSurvival;
        this.money = copy.money;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getSanity() {
        return sanity;
    }

    public void setSanity(int sanity) {
        this.sanity = sanity;
    }

    public int getAcademicSurvival() {
        return academicSurvival;
    }

    public void setAcademicSurvival(int academicSurvival) {
        this.academicSurvival = academicSurvival;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
