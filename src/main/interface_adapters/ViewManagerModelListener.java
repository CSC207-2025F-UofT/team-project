package main.interface_adapters;

//A layer of abstraction that lets viewManagerModel notify viewManager that the current view changed
public interface ViewManagerModelListener {

    void viewChanged();
}
