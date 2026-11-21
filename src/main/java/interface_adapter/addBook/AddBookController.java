package interface_adapter.addBook;
import entity.Book;
import use_case.addBook.AddBookInputBoundary;
import use_case.addBook.AddBookInputData;

/**
 * The controller for the Add Book use case.
 */
public class AddBookController {
     private final AddBookInputBoundary addBookInteractor;

     public AddBookController(AddBookInputBoundary addBookInteractor){
         this.addBookInteractor = addBookInteractor;
     }

    /**
     * Executes the Add Book Use Case.
     */
    public void execute(Book book){
        final AddBookInputData addBookInputData = new AddBookInputData(book);
        addBookInteractor.execute(addBookInputData);
    }


}
