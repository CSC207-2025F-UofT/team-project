package interface_adapter.addBook;
import use_case.addBook.AddBookInputBoundary;
import use_case.addBook.AddBookInputData

public class AddBookController {
     private final AddBookInputBoundary addBookInteractor;

     public AddBookController(AddBookInputBoundary addBookInteractor){
         this.addBookInteractor = addBookInteractor;
     }

    /**
     * Executes the Add Book Use Case.
     *
     */
    public void execute(Book book){

    }


}
