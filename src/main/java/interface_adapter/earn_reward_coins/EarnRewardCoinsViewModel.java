package interface_adapter.earn_reward_coins;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EarnRewardCoinsViewModel {
    private final EarnRewardCoinsState state = new EarnRewardCoinsState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public EarnRewardCoinsState getState(){
        return state;
    }

    public void firePropertyChanged(){
        support.firePropertyChange("state", null, state);
    }

    public void addPropertyChangedListener(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }
}
