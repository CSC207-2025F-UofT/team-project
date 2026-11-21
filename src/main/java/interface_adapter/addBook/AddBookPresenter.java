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
    void prepareSuccessView(AddBookOutputData response){
        this.addBookViewModel.setMessage(response.getTitle() + " by "+ response.getAuthor() +" has been successfully added to the shelf.");
    }

    /**
     * Prepares the failure view for the Add Book use case
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
