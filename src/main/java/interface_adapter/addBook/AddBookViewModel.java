package interface_adapter.addBook;

public class AddBookViewModel {
    private String message;
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
}
