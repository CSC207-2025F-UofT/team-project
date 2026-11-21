package interface_adapter.addBook;

import interface_adapter.ViewManagerModel;
import interface_adapter.addBook.AddBookViewModel;
import use_case.addBook.AddBookOutputBoundary;
import use_case.addBook.AddBookOutputData;

/**
 *The Presenter for the add book use case.
 */
public class AddBookPresenter implements AddBookOutputBoundary {
    private final AddBookViewModel addBookViewModel;

    public AddBookPresenter(AddBookViewModel addBookViewModel){
        this.addBookViewModel = addBookViewModel;
    }

    @Override
    public void prepareSuccessView(AddBookOutputData response){
        this.addBookViewModel.setMessage(response.getTitle() + " by "+ response.getAuthor() +" has been successfully added to the shelf.");
    }

    @Override
    public void prepareFailView(String errorMessage){
        this.addBookViewModel.setMessage(errorMessage);
    }
}
