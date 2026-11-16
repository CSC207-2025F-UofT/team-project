package org.example.cocktailcraft.interface_adapter;

/** Initial display state; each owner adds their feature's fields. */
public class MessageState {
    private String message = "Scaffold preview - feature not implemented.";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
