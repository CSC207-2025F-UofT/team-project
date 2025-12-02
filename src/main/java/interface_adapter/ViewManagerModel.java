package interface_adapter;

public class ViewManagerModel extends ViewModel<String> {
	private String windowTitle = "";
	 
    public ViewManagerModel() {
        super("view manager");
        this.setState("");
    }    

    /**
     * Sets the window title and notifies listeners of the change.
     *
     * @param title the new window title
     */
    public void setWindowTitle(String title) {
        this.windowTitle = title;
        firePropertyChange("title", null, this.windowTitle);
    }

    public String getWindowTitle() {
        return windowTitle;
    }
    
}
