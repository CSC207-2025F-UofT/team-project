package entity;

// anything the calculator says no to, badly written or just cant be done
public abstract class MathException extends RuntimeException {
    protected MathException(String message) {
        super(message);
    }
}
