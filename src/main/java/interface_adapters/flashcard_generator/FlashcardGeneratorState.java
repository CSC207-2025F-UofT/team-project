package interface_adapters.flashcard_generator;

/**
 * The State of the flashcard generator use case.
 */
public class FlashcardGeneratorState {

    private String message;
    private boolean success;


    public FlashcardGeneratorState(String message, boolean success){
        this.message = message;
        this.success = success;

    }

    /**
     * Returns current message of State
     * @return message of State
     */
    public String getMessage(){
        return this.message;
    }

    /**
     * Returns success of State
     * @return if State is successful
     */
    public boolean isSuccess(){
        return this.success;

    }

    /**
     * Set State message to current message
     * @param message the message to set State
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Set State to success
     * @param success current success of State
     */
    public void setSuccess(boolean success){
        this.success = success;
    }
}
