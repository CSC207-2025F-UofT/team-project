package interface_adapter;

import java.util.ArrayList;
import java.util.List;

public class ViewManagerModel {

    private String currentViewName = "";
    private List<ViewModelManagerListener> viewManagerListeners = new ArrayList<>();
    public ViewManagerModel() {}

    public String getCurrentViewName() {
        return currentViewName;
    }

    public void addListener(ViewModelManagerListener viewModelManagerListener){
        viewManagerListeners.add(viewModelManagerListener);
    }

    public void setState(String currentViewName) {
        this.currentViewName = currentViewName;
    }

    public void notifyViewChanged(){
        for (ViewModelManagerListener viewManagerListener : viewManagerListeners){
            viewManagerListener.viewChanged();
        }
    }
}
