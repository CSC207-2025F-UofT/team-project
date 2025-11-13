package interface_adapter.GameSelect;

/**
 * The state for the Login View Model.
 */
public class GameSelectState {
    private String game = "None ";
    private float stakes = 0;

    public String getGame() {
        return game;
    }

    public float getStakes() {
        return stakes;
    }

    public void setGame(String game){
        this.game = game;
    }
    public void setStakes(float stakes){
        this.stakes = stakes;
    }

}
